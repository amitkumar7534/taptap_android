package com.example.myapplication.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.ablanco.zoomy.Zoomy
import com.bumptech.glide.Glide
import com.example.myapplication.adapter.CommentAdapter
import com.example.myapplication.api.response.post.Data
import com.example.myapplication.ui.base.BindingActivity
import com.example.myapplication.utils.Constant
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.like.LikeButton
import com.like.OnLikeListener
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.ActivityPostDetailBinding
import com.yookoo.discoveraddis.databinding.BottomSheetCommentsBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class PostDetailActivity : BindingActivity<ActivityPostDetailBinding>() {
    lateinit var data: Data
    var count = 0
    override fun getLayout(): Int {
        return R.layout.activity_post_detail
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
          data = intent.getSerializableExtra("data") as Data

        viewPost(data.id.toString(),appSession()
            .getString(Constant.USER_ID).toString())

        Glide.with(this)
            .load(data.post_image)
            .placeholder(R.drawable.discoveraddislogo_new)
            .into(viewBinding.bookedmenuPicImg)


        val builder = Zoomy.Builder(this).target(viewBinding.bookedmenuPicImg)
        builder.register()


        Glide.with(this)
            .load(data.user_image)
            .placeholder(R.drawable.discoveraddislogo_new)
            .into(viewBinding.userImage)
        if (data.post_type=="post"){
            viewBinding.productPrice.visibility= View.GONE
        }else{
            viewBinding.productPrice.visibility= View.VISIBLE

        }
        count =data.favorites_count
        viewBinding.bookedmenuNameInc.text = data.post_title
        viewBinding.decription.text = data.description
        viewBinding.userName.text = data.name
        viewBinding.productPrice.text ="Price - "+data.price.toString()+" Br"
        viewBinding.likeCount.text = "Like ($count)"
        viewBinding.viewCount.text ="View ("+data.review_count.toString()+")"
        viewBinding.commentCount.text ="Comment ("+data.comment_count.toString()+")"
        viewBinding.postTime.text=covertTimeToText(data.created_at)
        viewBinding.likeButton.isLiked = data.is_liked=="1"


        viewBinding.commentCount.setOnClickListener {
            showCommentDialog(0,data)
        }



        viewBinding.likeButton.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton) {
                count=data.favorites_count
                count += 1
                viewBinding.likeCount.text = "Like ($count)"
                likePost(data.id.toString(),appSession()
                    .getString(Constant.USER_ID).toString())
            }
            override fun unLiked(likeButton: LikeButton) {
                if (count>=1) {
                    count -= 1
                    viewBinding.likeCount.text = "Like ($count)"
                    likePost(data.id.toString(),appSession()
                        .getString(Constant.USER_ID).toString())
                }
            }
        })








        viewBinding.btnBackTomain.setOnClickListener {
            finish()
        }

       

    }


    fun likePost(id:String,uid:String){
        baseViewModel.repository.likePost(apiListener(),id,uid).observe(this) {
            if(it.success){

            }
        }
    }


    fun covertTimeToText(dataDate: String?): String? {
        var convTime: String? = null
        val prefix = ""
        val suffix = getString(R.string.ago)
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            dateFormat.timeZone= TimeZone.getTimeZone("UTC")
            val pasTime = dateFormat.parse(dataDate)
            val nowTime = Date()
            val dateDiff = nowTime.time - pasTime.time
            val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
            if (second < 60) {
                convTime = "$second "+ getString(R.string.seconds) +" $suffix"
            } else if (minute < 60) {
                convTime = "$minute "+ getString(R.string.minutes) +" $suffix"
            } else if (hour < 24) {
                convTime = "$hour "+ getString(R.string.hours) +" $suffix"
            } else if (day >= 7) {
                convTime = if (day > 360) {
                    (day / 360).toString() + " "+ getString(R.string.years) + " " + suffix
                } else if (day > 30) {
                    (day / 30).toString() + " "+ getString(R.string.month) + " " + suffix
                } else {
                    (day / 7).toString() + " "+ getString(R.string.weak) + " " + suffix
                }
            } else if (day < 7) {
                convTime = "$day "+ getString(R.string.days) +" $suffix"
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.e("ConvTimeE", e.message.toString())
        }
        return convTime
    }



    fun viewPost(id:String,uid:String){
        baseViewModel.repository.viewPost(apiListener(),id,uid).observe(this) {
            if(it.success){
            }
        }
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
//                    binding.rvComments.scrollToPosition(it.data.size-1)

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
                            binding.rvComments.scrollToPosition(it.data.size-1)
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
//          showToast(it.message)
            }
        }
    }


}

