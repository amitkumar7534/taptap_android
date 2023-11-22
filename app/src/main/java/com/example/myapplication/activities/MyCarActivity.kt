package com.example.myapplication.activities

import android.os.Bundle
import android.view.View
import com.example.myapplication.adapter.CarAdapter
import com.example.myapplication.api.response.business.Data
import com.example.myapplication.ui.base.BindingActivity
import com.example.myapplication.ui.callback.ViewClickCallback
import com.example.myapplication.utils.Constant
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.ActivityMyCarBinding

class MyCarActivity : BindingActivity<ActivityMyCarBinding>() {
    lateinit var data: Data

    override fun getLayout(): Int {
        return R.layout.activity_my_car
    }

    var adapter: CarAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding.ivBack.setOnClickListener {
            finish()
        }
    }


    override fun onResume() {
        super.onResume()
        getOwnCar(appSession().getString(Constant.USER_PHONE_NUMBER).toString(),"vehicle")
    }

    fun getOwnCar(no: String, name: String) {
        baseViewModel.repository.getOWNCar(apiListener(), appSession().getString(Constant.USER_PHONE_NUMBER).toString(),"vehicle").observe(this) {
            hideProgress()
            if(it.success){
                if(it.data.size == 0){
                    showToast("No Data Found!")
                    viewBinding!!.carRecyclerView.visibility= View.GONE
                }else{
                    viewBinding!!.carRecyclerView.visibility= View.VISIBLE
                    adapter = CarAdapter(this,it.data,object: ViewClickCallback {
                        override fun onClick(position: Int, id: String,type:String) {
                            showDeleteDialog(id)
                        }
                    })
                    viewBinding!!.carRecyclerView.adapter = adapter

                }
            }
        }
    }

    fun showDeleteDialog(id:String){
        val builder = android.app.AlertDialog.Builder(activity)
        builder.setMessage(getString(R.string.delete_car))
        builder.setPositiveButton(getString(R.string.yes)
        ) { dialog, which ->
            showProgress()
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
        baseViewModel.repository.deleteCarBusiness(apiListener(),id).observe(this) {
           hideProgress()
            if(it.success){
                getOwnCar(appSession().getString(Constant.USER_PHONE_NUMBER).toString(),"vehicle")
            }
        }
    }

}