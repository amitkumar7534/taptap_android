package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.NotificationAdapter
import com.example.myapplication.api.response.NotificationResponse
import com.example.myapplication.ui.base.BindingActivity
import com.example.myapplication.utils.Constant
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.ActivityNotificationBinding

class NotificationActivity : BindingActivity<ActivityNotificationBinding>(),NotificationAdapter.DeleteNotification {

    override fun getLayout(): Int {
        return R.layout.activity_notification
    }

    private var data = mutableListOf<NotificationResponse.Datum>()
    var Layout: LinearLayoutManager? = null

    var notification: NotificationAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding.ivBack.setOnClickListener {
            finish()
        }
        viewBinding.folowRequestView.setOnClickListener {
            val i = Intent(this, CommonActivity::class.java)
            i.putExtra(getString(R.string.fragmentName), getString(R.string.follow_fragment))
            startActivity(i)
        }
        viewBinding.readAll.setOnClickListener {
            showProgress()
            readAll()
        }
        SetAdapter()
        getNotification()
    }

    fun SetAdapter() {
        Layout = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false)
        viewBinding.notificationRecyclerView.setLayoutManager(Layout)
        notification = NotificationAdapter(this, data,this)
        viewBinding.notificationRecyclerView.adapter = notification
    }

    fun getNotification() {
        baseViewModel.repository.getNotification(
            apiListener(),
            appSession().getString(Constant.USER_ID).toString()
        ).observe(this) {
            hideProgress()
            if (it.success == true) {
                if (it.data?.size!! > 0) {
                    if (data.size > 0) {
                        data.clear()
                    }
                    it.data.let { it?.let { it1 -> data.addAll(it1) } }
                    viewBinding.llNoData.visibility=View.GONE
                    viewBinding.readAll.visibility=View.VISIBLE
                    notification?.notifyDataSetChanged()
                }else{
                    viewBinding.llNoData.visibility=View.VISIBLE
                    viewBinding.readAll.visibility=View.GONE
                }
            } else {
                showToast(it.message.toString())
            }
        }
    }

    fun readAll() {
        baseViewModel.repository.readAllNotification(
            apiListener(),
            appSession().getString(Constant.USER_ID).toString()
        ).observe(this) {
            hideProgress()
            if (it.success == true) {
                getNotification()
            } else {
                showToast(it.message.toString())
            }
        }
    }

    override fun delete(position: Int, Id: String) {
        remove_notification(position)
    }

    fun remove_notification(position :Int){
        val builder = android.app.AlertDialog.Builder(activity)
        builder.setTitle("Delete")
        builder.setMessage(getString(R.string._delete_this_notification))
        builder.setPositiveButton(getString(R.string.yes)) { dialog, which ->
            data.removeAt(position)
            notification?.notifyDataSetChanged()
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

}