package com.example.myapplication.fragments.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ablanco.zoomy.Zoomy
import com.devs.readmoreoption.ReadMoreOption
import com.example.myapplication.activities.UpdatePostActivity
import com.example.myapplication.adapter.CommentAdapter
import com.example.myapplication.adapter.PostAdapter
import com.example.myapplication.adapter.onthreeDots
import com.example.myapplication.api.response.post.Data
import com.example.myapplication.fragments.HomeFragment
import com.example.myapplication.ui.base.BaseFragment
import com.example.myapplication.utils.Constant
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.madapps.liquid.LiquidRefreshLayout
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.BottomSheetCommentsBinding
import com.yookoo.discoveraddis.databinding.FragmentPostBinding
import com.yookoo.discoveraddis.databinding.ReportPopupBinding


class PostFragment : BaseFragment<FragmentPostBinding>(), onthreeDots {

    override fun getLayout(): Int {
        return R.layout.fragment_post
    }

    var myList = ArrayList<Data>()
    var isLoad = true
    lateinit var postAdapter: PostAdapter

    var count = 1

    private var instance: HomeFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        instance = this

    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        postAdapter = PostAdapter(requireActivity(), this, myList as ArrayList<Data>)
//        tBinding.postRecyclerView.adapter = postAdapter
//        myList.clear()
        val fm: FragmentManager? = fragmentManager
        val fragm: HomeFragment? =
            fm!!.findFragmentById(R.id.container) as HomeFragment?

        postAdapter = PostAdapter(requireActivity(), this, myList as ArrayList<Data>)
        tBinding.postRecyclerView.adapter = postAdapter


        val myLayoutManager: LinearLayoutManager =
            tBinding.postRecyclerView.layoutManager as LinearLayoutManager
        tBinding.postRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
//                if (dy > 0) {
//                    // Scrolling up
//                    val scrollPosition = myLayoutManager.findLastVisibleItemPosition()
//                    val totalItemCount: Int = myLayoutManager!!.itemCount
//
//                    Log.e("Csdcdscsd", "$isLoad")
//
//                    if (scrollPosition == totalItemCount - 1) {
//                    }
//                } else {
//                    // Scrolling down
//                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Do something
                    fragm?.hideToolbar()
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // Do something

                    if (isLoad == true) {
                        count += 1
//                        tBinding.idPBLoading.visibility = View.VISIBLE
                        getPosts(count.toString(), "")
                    }


//                    if (!recyclerView.canScrollVertically(1)) {
////                        Toast.makeText(requireActivity(), "Last record", Toast.LENGTH_LONG).show();
//                        if (isLoad == true) {
//                            count += 1
////                        tBinding.idPBLoading.visibility = View.VISIBLE
//                            getPosts(count.toString(), "")
//                        }
//                    }


                } else {
                    // Do something
                    fragm?.ShowToolbar()
                }
            }
        })


//        tBinding.postRecyclerView.hasFixedSize()
//        tBinding.postRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//
//
//                if (dy > 0) //check for scroll down
//                {
//                    fragm?.hideToolbar()
//
//
//
//                } else {
//                    val handler = Handler(Looper.getMainLooper())
//                    handler.postDelayed({
//                        fragm?.ShowToolbar()
//                    }, 3000)
//
//                }
//            }
//        })

        /*
                tBinding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                    // on scroll change we are checking when users scroll as bottom.
                    if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                        // in this method we are incrementing page number,
                        // making progress bar visible and calling get data method.

                        if (isLoad) {
                            tBinding.idPBLoading.visibility = View.VISIBLE
                            getPosts(count.toString(),"")
                        }else{
                            tBinding.idPBLoading.visibility = View.GONE

                        }
                    }
                })
        */


        getPostData(count)
        tBinding.refresh.setOnRefreshListener(object : LiquidRefreshLayout.OnRefreshListener {
            override fun completeRefresh() {
                //Called when you call refreshLayout.finishRefreshing()
            }

            override fun refreshing() {
                isLoad = true
                //TODO make api call here

                count = 1
                getPostData(count)
            }
        })
    }

    fun getInstance(): HomeFragment? {
        return instance
    }

    override fun onResume() {
        super.onResume()
    }

    fun getPostData(count: Int) {
        getPosts(count.toString(), "")
    }


    fun isLastVisible(): Boolean {
        val layoutManager = tBinding.postRecyclerView.getLayoutManager() as LinearLayoutManager
        val pos = layoutManager.findLastCompletelyVisibleItemPosition()
        val numItems: Int = tBinding.postRecyclerView.adapter?.getItemCount()!!
        return pos >= numItems
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("NotifyDataSetChanged")
    fun getPosts(id: String, type: String) {
        getBaseActivity().baseViewModel.repository.getPosts(
            apiListener(), id, getBaseActivity().appSession().getString(
                Constant.USER_ID
            ).toString()
        ).observe(viewLifecycleOwner) {
            if (it.success) {
//                tBinding.idPBLoading?.visibility = View.GONE
                tBinding.idLoading?.visibility = View.GONE
                tBinding.refresh?.finishRefreshing()

                if (id.equals("1")) {
                    if (myList.size > 0) {
                        myList.clear()
                    }
                }

                if (it.data.size > 0) {
                    isLoad = true
                    myList.addAll(it.data as ArrayList<Data>)
                    postAdapter.notifyDataSetChanged()
                    if (myList.isNotEmpty()) {
                        tBinding.tvNoData?.visibility = View.GONE
                    }
                } else {
                    Log.e("cxzcczx", "jbjcbnbn")
                    isLoad = false
                }
            }
        }
    }


    fun deletePost(id: String, postion: Int) {
        getBaseActivity().baseViewModel.repository.deletePost(apiListener(), id)
            .observe(viewLifecycleOwner) {
                if (it.success) {
                    count = 1
                    getBaseActivity().showToast(it.message)
                    myList.removeAt(postion)
                    postAdapter.notifyDataSetChanged()
                    //   getPosts(count.toString(),"")
                }
            }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun likePost(id: String, uid: String) {
        getBaseActivity().baseViewModel.repository.likePost(apiListener(), id, uid)
            .observe(viewLifecycleOwner) {
                if (it.success) {
//                   / getPostData()
                    // getPosts(count.toString(),"like")
                }
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun hidePost(id: String, uid: String, postion: Int) {
        getBaseActivity().baseViewModel.repository.hidePost(apiListener(), id, uid)
            .observe(viewLifecycleOwner) {
                if (it.success) {
                    getBaseActivity().showToast(it.message)
                    myList.removeAt(postion)
                    postAdapter.notifyDataSetChanged()
                    //   getPosts(count.toString(),"")
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun reportPost(id: String, uid: String, message: String) {
        getBaseActivity().baseViewModel.repository.reportPost(apiListener(), id, uid, message)
            .observe(viewLifecycleOwner) {
                if (it.success) {
                    getBaseActivity().showToast(it.message)
                    //   getPosts(count.toString(),"like")
                }
            }
    }

    fun addComment(id: String, uid: String, message: String) {
        getBaseActivity().baseViewModel.repository.addComment(apiListener(), id, uid, message)
            .observe(viewLifecycleOwner) {
                if (it.success) {
//                getBaseActivity().showToast(it.message)
                    //getPosts(count.toString())
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("MissingInflatedId")
    override fun DotClick(postion: Int, data: Data, type: String) {
        if (type == "like") {
            likePost(
                data.id.toString(), getBaseActivity().appSession()
                    .getString(Constant.USER_ID).toString()
            )
        } else if (type == "comment") {
            showCommentDialog(postion, data)


        } else {
            showBottomDialog(postion, data)
        }
    }

    override fun imageClick(postion: Int, imageview: ImageView) {
        if (activity != null) {
            val builder = Zoomy.Builder(activity).target(imageview)
            builder.register()
        }
    }

    override fun settext(postion: Int, textView: TextView, description: String) {
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

        readMoreOption.addReadMoreTo(textView, description)


    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun showBottomDialog(postion: Int, data: Data) {
        val dialog = BottomSheetDialog(requireActivity())
        // on below line we are inflating a layout file which we have created.
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
        val btnClose = view.findViewById<TextView>(R.id.delete_sheet)
        val rlEdit = view.findViewById<RelativeLayout>(R.id.rl_edit)
        val rlDelete = view.findViewById<RelativeLayout>(R.id.rl_delete)
        val rlHide = view.findViewById<RelativeLayout>(R.id.rl_hide)
        val rlReport = view.findViewById<RelativeLayout>(R.id.rl_report)

        if (data.user_id.toString() != getBaseActivity().appSession()
                .getString(Constant.USER_ID).toString()
        ) {
            rlEdit.visibility = View.GONE
            rlDelete.visibility = View.GONE
            rlHide.visibility = View.VISIBLE
            rlReport.visibility = View.VISIBLE
        } else {
            rlEdit.visibility = View.VISIBLE
            rlDelete.visibility = View.VISIBLE
            rlHide.visibility = View.GONE
            rlReport.visibility = View.GONE
        }
        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        rlDelete.setOnClickListener {
            dialog.dismiss()
            showLogoutDialog(data.id.toString(), postion)
        }
        rlHide.setOnClickListener {
            dialog.dismiss()
            showHideDialog(data.id.toString(), postion)
        }
        rlReport.setOnClickListener {
            dialog.dismiss()
            showDialog(data.id.toString())
        }
        rlEdit.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(requireContext(), UpdatePostActivity::class.java)
            intent.putExtra("data", data)
            startActivity(intent)

        }

        dialog.setContentView(view)
        dialog.show()
    }

    fun showCommentDialog(postion: Int, data: Data) {
        val dialog = BottomSheetDialog(requireActivity())
        // on below line we are inflating a layout file which we have created.
        val binding: BottomSheetCommentsBinding =
            BottomSheetCommentsBinding.inflate(LayoutInflater.from(context))
        getBaseActivity().baseViewModel.repository.getComment(apiListener(), data.id.toString())
            .observe(viewLifecycleOwner) {
                if (it.success) {
                    if (it.data.isEmpty()) {
                        binding.noComment.visibility = View.VISIBLE

                    } else {
                        binding.noComment.visibility = View.GONE
                        binding.rvComments.adapter = CommentAdapter(
                            requireActivity(),
                            it.data as ArrayList<com.example.myapplication.api.response.comment.Data>

                        )

                    }
                }
            }
        binding.ivSend.setOnClickListener {
            if (binding.etMessage.text.toString() == "") {
                getBaseActivity().showToast("Please write your comment")
            } else {
                addComment(
                    data.id.toString(), getBaseActivity().appSession()
                        .getString(Constant.USER_ID).toString(), binding.etMessage.text.toString()
                )
                binding.etMessage.setText("")

                getBaseActivity().baseViewModel.repository.getComment(
                    apiListener(),
                    data.id.toString()
                ).observe(viewLifecycleOwner) {
                    if (it.success) {
                        if (it.data.isEmpty()) {
                            binding.noComment.visibility = View.VISIBLE

                        } else {
                            binding.noComment.visibility = View.GONE
                            binding.rvComments.adapter = CommentAdapter(
                                requireActivity(),
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


    fun showLogoutDialog(id: String, postion: Int) {
        val builder = android.app.AlertDialog.Builder(activity)
        builder.setMessage("Are you sure you want to delete post?")
        builder.setPositiveButton(
            getString(R.string.yes)
        ) { dialog, which ->
            deletePost(id, postion)
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


    @SuppressLint("NotifyDataSetChanged")
    fun showHideDialog(id: String, postion: Int) {
        val builder = android.app.AlertDialog.Builder(activity)
        builder.setMessage("Are you sure you want to hide post?")
        builder.setPositiveButton(
            getString(R.string.yes)
        ) { dialog, which ->
            myList.removeAt(postion)
            postAdapter.notifyItemRemoved(postion)
            postAdapter.notifyDataSetChanged()
            hidePost(
                id, getBaseActivity().appSession().getString(
                    Constant.USER_ID
                ).toString(), postion
            )
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


    @RequiresApi(Build.VERSION_CODES.N)
    fun showDialog(id: String) {
        val dialog = Dialog(requireContext())
        val binding: ReportPopupBinding = ReportPopupBinding.inflate(LayoutInflater.from(context))
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(binding.root)
        binding.radioGroup1.setOnCheckedChangeListener { radioGroup, i ->
            if (i == R.id.radioButton4) {
                binding.edtModel.visibility = View.VISIBLE
            } else {
                binding.edtModel.visibility = View.GONE

            }
        }
        binding.submitButton.setOnClickListener {
            if (binding.radioButton4.isChecked) {
                if (binding.edtModel.text.toString() == "") {
                    Toast.makeText(
                        requireContext(),
                        "Please enter report reason",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    dialog.dismiss()
                    reportPost(
                        id, getBaseActivity().appSession().getString(
                            Constant.USER_ID
                        ).toString(), binding.edtModel.text.toString()
                    )
                }
            } else {
                val selectedId: Int = binding.radioGroup1.checkedRadioButtonId
                val radioButton = dialog.findViewById(selectedId) as RadioButton
                val ac = radioButton.text.toString()
                reportPost(
                    id, getBaseActivity().appSession().getString(
                        Constant.USER_ID
                    ).toString(), ac
                )
                dialog.dismiss()

            }
        }
        dialog.show()
    }


}