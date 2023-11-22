package com.example.myapplication.login

import android.content.Intent
import android.os.Bundle
import com.example.myapplication.activities.BusinessesDashboardActivity
import com.example.myapplication.ui.base.BindingActivity
import com.example.myapplication.utils.Constant
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.ActivityOtpBinding


class OtpActivity : BindingActivity<ActivityOtpBinding>() {


    override fun getLayout(): Int {
        return R.layout.activity_otp
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        val otp = intent.getStringExtra("otp")
        val number = intent.getStringExtra("number")
        val user_id = intent.getStringExtra("user_id")

        viewBinding.phoneNumber.setText(number.toString())
        viewBinding.otp1.setText(otp.toString().substring(0, 1).toString())
        viewBinding.otp2.setText(otp.toString().substring(1, 2).toString())
        viewBinding.otp3.setText(otp.toString().substring(2, 3).toString())
        viewBinding.otp4.setText(otp.toString().substring(3, 4).toString())
        viewBinding.otp5.setText(otp.toString().substring(4, 5).toString())
        viewBinding.otp6.setText(otp.toString().substring(5, 6).toString())

        viewBinding.submitOtp.setOnClickListener {
            showProgress()
            OTPVerification(user_id.toString(), otp.toString())
        }

    }

    fun OTPVerification(id: String, otp: String) {
        baseViewModel.repository.VerifyOTP(apiListener(), id, otp).observe(this) {
            hideProgress()
            if (it.success == true) {
                showToast(it.message.toString())
                appSession().save("login_value", true)
                appSession().save(Constant.USER_NAME, it?.data?.full_name.toString())
                appSession().save(Constant.USER_PHONE_NUMBER, it?.data?.phone_no.toString())
                appSession().save(Constant.USER_PICTURE, it?.data?.profile_pic?.toString())
                appSession().save(Constant.USER_ID, it?.data?._id?.toString())
                val intent = Intent(this, BusinessesDashboardActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            } else {
                showToast(it.message.toString())
            }
        }
    }


}