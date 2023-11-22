package com.example.myapplication.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.ablanco.zoomy.Zoomy
import com.devs.readmoreoption.ReadMoreOption
import com.example.myapplication.adapter.CommentAdapter
import com.example.myapplication.adapter.PostAdapter
import com.example.myapplication.adapter.onthreeDots
import com.example.myapplication.api.response.post.Data
import com.example.myapplication.ui.base.BindingActivity
import com.example.myapplication.utils.Constant
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.ActivityMyPostBinding
import com.yookoo.discoveraddis.databinding.BottomSheetCommentsBinding


class MyPostActivity :BindingActivity<ActivityMyPostBinding> (),onthreeDots {
    lateinit var postAdapter:PostAdapter
    override fun getLayout(): Int {
        return R.layout.activity_my_post
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding.ivBack.setOnClickListener {
            onBackPressed()
        }

//        Log.e("fvbfdsvfd",challenge.get.toString())


        getProfile(intent.getStringExtra("id").toString())

    }

    fun getProfile(id:String){

        var postion = intent.getIntExtra("pos",0)
        Log.e("edwc",postion.toString())
        viewBinding.postRecyclerView.scrollToPosition(postion)

        baseViewModel.repository.getProfile(apiListener(),id,appSession().getString(Constant.USER_ID).toString()).observe(this) {
                postAdapter = PostAdapter(this,this,it.posts as ArrayList<Data>)
                viewBinding!!.postRecyclerView.adapter = postAdapter
                Log.e("edwc",intent.getIntExtra("pos",0).toString())
        }

    }

    fun deletePost(id:String){
        baseViewModel.repository.deletePost(apiListener(),id).observe(this) {
            if(it.success){
                getProfile(intent.getStringExtra("id").toString())
            }
        }
    }

    fun likePost(id:String,uid:String){
        baseViewModel.repository.likePost(apiListener(),id,uid).observe(this) {
            if(it.success){

            }
        }
    }

    fun viewPost(id:String,uid:String){
        baseViewModel.repository.viewPost(apiListener(),id,uid).observe(this) {
            if(it.success){

            }
        }
    }

    override fun DotClick(postion: Int, data: Data,type:String) {
        if (type=="like"){
            likePost(data.id.toString(),appSession()
                .getString(Constant.USER_ID).toString())
        }else if (type=="view"){
            viewPost(data.id.toString(),appSession()
                .getString(Constant.USER_ID).toString())
            val intent = Intent(this, PostDetailActivity::class.java)
            intent.putExtra("data",data)
            startActivity(intent)
        }else if (type=="comment"){
            showCommentDialog(postion, data)


        }else {
            val dialog = BottomSheetDialog(this)
            // on below line we are inflating a layout file which we have created.
            val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
            val btnClose = view.findViewById<TextView>(R.id.delete_sheet)
            val rlEdit = view.findViewById<RelativeLayout>(R.id.rl_edit)
            val rlDelete = view.findViewById<RelativeLayout>(R.id.rl_delete)

            if (data.user_id.toString() != appSession()
                    .getString(Constant.USER_ID).toString()
            ) {
                rlEdit.visibility = View.GONE
                rlDelete.visibility = View.GONE
            } else {
                rlEdit.visibility = View.VISIBLE
                rlDelete.visibility = View.VISIBLE
            }
            btnClose.setOnClickListener {
                dialog.dismiss()
            }
            rlDelete.setOnClickListener {
                dialog.dismiss()
                showLogoutDialog(data.id.toString())
            }
            rlEdit.setOnClickListener {
                dialog.dismiss()
                val intent = Intent(this, UpdatePostActivity::class.java)
                intent.putExtra("data", data)
                startActivity(intent)

            }

            dialog.setContentView(view)
            dialog.show()
        }
    }

    override fun imageClick(postion: Int, imageview: ImageView) {
        if (activity != null){
            val builder = Zoomy.Builder(activity).target(imageview)
            builder.register()
        }
    }

    override fun settext(postion: Int, imageview: TextView, description: String) {
        val readMoreOption = ReadMoreOption.Builder(activity)
            .textLength(2, ReadMoreOption.TYPE_LINE) // OR
            //.textLength(300, ReadMoreOption.TYPE_CHARACTER)
            .moreLabel(" MORE")
            .lessLabel(" LESS")
            .moreLabelColor(Color.RED)
            .lessLabelColor(Color.BLUE)
            .labelUnderLine(true)
            .expandAnimation(true)
            .build()

        readMoreOption.addReadMoreTo(imageview, description)
    }

    fun showCommentDialog(postion: Int, data: Data){
        val dialog = BottomSheetDialog(this)
        // on below line we are inflating a layout file which we have created.
        val binding: BottomSheetCommentsBinding = BottomSheetCommentsBinding.inflate(LayoutInflater.from(this))
        baseViewModel.repository.getComment(apiListener(),data.id.toString()).observe(this) {
            if(it.success){
                if (it.data.isEmpty()){
                    binding.noComment.visibility=View.VISIBLE

                }else {
                    binding.noComment.visibility=View.GONE
                    binding.rvComments.adapter = CommentAdapter(
                        this,
                        it.data as ArrayList<com.example.myapplication.api.response.comment.Data>

                    )

                }
            }
        }
        binding.ivSend.setOnClickListener {
            if (binding.etMessage.text.toString()==""){
                showToast("Please write your comment")
            }else{
                addComment(data.id.toString(),appSession()
                    .getString(Constant.USER_ID).toString(),binding.etMessage.text.toString())
                binding.etMessage.setText("")

                baseViewModel.repository.getComment(apiListener(),data.id.toString()).observe(this) {
                    if(it.success){
                        if (it.data.isEmpty()){
                            binding.noComment.visibility=View.VISIBLE

                        }else {
                            binding.noComment.visibility=View.GONE
                            binding.rvComments.adapter = CommentAdapter(
                                this,
                                it.data as ArrayList<com.example.myapplication.api.response.comment.Data>
                            )
                        }
                    }
                }
            }
        }


        dialog.setContentView(binding.root)
        dialog.show()
    }


    fun addComment(id:String,uid:String,message:String){
        baseViewModel.repository.addComment(apiListener(),id,uid, message).observe(this) {
            if(it.success){
//                showToast(it.message)
                //getPosts(count.toString())
            }
        }
    }


    fun showLogoutDialog(id:String){
        val builder = android.app.AlertDialog.Builder(activity)
        builder.setMessage("Are you sure you want to delete post?")
        builder.setPositiveButton(getString(R.string.yes)
        ) { dialog, which ->
            deletePost(id)
        }
        builder.setNegativeButton(getString(R.string.no), null)

        val dialog = builder.create()
        dialog.setOnShowListener {
            dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
                .setTextColor(resources.getColor(R.color.black))
            dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(resources.getColor(R.color.black))
        }
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
      //  getProfile(appSession().getString(Constant.USER_ID).toString())

    }
}