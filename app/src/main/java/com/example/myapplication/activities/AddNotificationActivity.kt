package com.example.myapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.login.LoginActivity
import com.example.myapplication.ui.base.BindingActivity
import com.example.myapplication.utils.Constant
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.ActivityAddNotificationBinding

class AddNotificationActivity : BindingActivity<ActivityAddNotificationBinding>() {

    override fun getLayout(): Int {
        return R.layout.activity_add_notification
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding.ivBack.setOnClickListener {
            finish()
        }

        viewBinding.submitButton.setOnClickListener {
            submit_notification()
        }
    }

    fun submit_notification() {
        baseViewModel.repository.push_notification(
            apiListener(),viewBinding.titleName.text.toString(),viewBinding.message.text.toString(),
            appSession().getString("token").toString())
            .observe(this) {
                hideProgress()
                showToast("Notification Sent")
                finish()
            }
    }



}