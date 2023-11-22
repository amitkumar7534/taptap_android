package com.example.myapplication.adapter

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.myapplication.api.response.post.Data
import com.example.myapplication.ui.base.BindingActivity
import com.example.myapplication.utils.Constant
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.ActivityProfile2Binding

class ProfileActivity : BindingActivity<ActivityProfile2Binding>() {

    lateinit var imagesAdapter: OwnPostAdapter

    override fun getLayout(): Int {
        return R.layout.activity_profile2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewBinding.setting.setOnClickListener {
           onBackPressed()
        }

        getProfile(intent.getStringExtra("id").toString())

    }


    fun getProfile(id: String) {
        baseViewModel.repository.getProfile(apiListener(), id, appSession().getString(Constant.USER_ID).toString()).observe(this) {


            if (it.data._id.toString().equals(appSession().getString(Constant.USER_ID).toString())){
                viewBinding.messageFollowView.visibility=View.GONE
            }else{
                viewBinding.messageFollowView.visibility=View.VISIBLE
            }

            if (it.posts.isEmpty()) {
                viewBinding.tvName.text = it.data.full_name
                viewBinding.tvPostCount.text = it.post_count.toString()
                viewBinding.tvProductCount.text = it.product_count.toString()
                viewBinding.tvName.text = it.data.full_name
                if (!it.data.profile_pic.isNullOrEmpty()) {
                    loadGlideInCircle(
                        it.data.profile_pic,
                        viewBinding.userImage,
                        R.drawable.iv_default_user
                    )
                }
                viewBinding.tvNoData.visibility = View.VISIBLE
                viewBinding.rvGallery.visibility = View.GONE
            } else {

                viewBinding.tvNoData.visibility = View.GONE
                viewBinding.rvGallery.visibility = View.VISIBLE
                viewBinding.tvName.text = it.data.full_name
                viewBinding.tvPostCount.text = it.post_count.toString()
                viewBinding.tvProductCount.text = it.product_count.toString()
                if (!it.data.profile_pic.isNullOrEmpty()) {
                    loadGlideInCircle(
                        it.data.profile_pic,
                        viewBinding.userImage,
                        R.drawable.iv_default_user
                    )
                }
                imagesAdapter = OwnPostAdapter(this, it.posts as ArrayList<Data>,intent.getStringExtra("id").toString())
                viewBinding!!.rvGallery.adapter = imagesAdapter
            }
        }

    }


    fun logout() {
        baseViewModel.repository.logout(
            apiListener(), appSession().getString(
                Constant.USER_ID
            ).toString()
        ).observe(this) {
            if (it.success) {
                showToast(it.message)
            }
        }
    }

}
