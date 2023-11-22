package com.example.myapplication.activities

import android.os.Bundle
import com.example.myapplication.adapter.MyBusinessAdapter
import com.example.myapplication.api.response.business.Data
import com.example.myapplication.ui.base.BindingActivity
import com.example.myapplication.ui.callback.ViewClickCallback
import com.example.myapplication.utils.Constant
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.ActivityMyBussinessBinding

class MyBussinessActivity : BindingActivity<ActivityMyBussinessBinding>() {
    lateinit var data: Data

    override fun getLayout(): Int {
        return R.layout.activity_my_bussiness
    }


    var adapter: MyBusinessAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding.ivBack.setOnClickListener {
            finish()
        }
        getOwnBusiness("","")
    }


    fun getOwnBusiness(no: String, name: String) {
        baseViewModel.repository.getOWNBusiness(apiListener(), appSession().getString(Constant.USER_PHONE_NUMBER).toString(),"business").observe(this) {
            hideProgress()
            if(it.success){
                if(it.data.size == 0){
                    showToast("No Data Found!")
                }else{
                    adapter = MyBusinessAdapter(this,it.data,object: ViewClickCallback {
                        override fun onClick(position: Int, id: String,type:String) {
                            showDeleteDialog(id)
                        }
                    })
                    viewBinding!!.businessRecyclerView.adapter = adapter

                }
            }
        }
    }
    fun showDeleteDialog(id:String){
        val builder = android.app.AlertDialog.Builder(activity)
        builder.setMessage(getString(R.string.are_you_logout))
        builder.setPositiveButton(getString(R.string.yes)
        ) { dialog, which ->
            deleteBusinesses(id)
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

    fun deleteBusinesses(id:String){
        baseViewModel.repository.deleteBusiness(apiListener(),id).observe(this) {
            if(it.success){
                getOwnBusiness("","")
            }
        }
    }



}