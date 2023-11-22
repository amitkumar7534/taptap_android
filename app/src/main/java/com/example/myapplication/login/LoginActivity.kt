package com.example.myapplication.login

import android.content.Intent
import android.os.Bundle
import com.example.myapplication.ui.base.BindingActivity
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.ActivityLoginBinding

class LoginActivity : BindingActivity<ActivityLoginBinding>() {

    override fun getLayout(): Int {
        return R.layout.activity_login
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding.loginButton.setOnClickListener {
            if (viewBinding.edtName.text.toString().equals("")) {
                showToast("Please enter your name!")
            } else if (viewBinding.edtPhone.text.toString().equals("")) {
                showToast("Please enter your phone number!")
            } else if (viewBinding.edtPhone.text.toString().length < 10) {
                showToast("Please check phone number!")
            } else {

                showProgress()
                login(viewBinding.edtPhone.text.toString(), viewBinding.edtName.text.toString())

            }
        }
    }

    fun login(no: String, name: String) {
        baseViewModel.repository.login(apiListener(), no, name, "android", appSession().getString("token").toString()).observe(this) {
            hideProgress()
            if (it.success == true) {
                showToast(it.message.toString())
                val intent = Intent(this, OtpActivity::class.java)
                intent.putExtra("number", viewBinding.edtPhone.text.toString())
                intent.putExtra("otp", it.data.otp.toString())
                intent.putExtra("user_id", it.data._id.toString())
                startActivity(intent)
            } else {
                showToast(it.message.toString())
            }
        }
    }


}