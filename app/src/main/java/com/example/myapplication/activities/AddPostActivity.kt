package com.example.myapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.ui.base.BindingActivity
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.ActivityAddNotificationBinding
import com.yookoo.discoveraddis.databinding.ActivityAddPostBinding

class AddPostActivity : BindingActivity<ActivityAddPostBinding>() {

    override fun getLayout(): Int {
        return R.layout.activity_add_post
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding.ivBack.setOnClickListener {
            finish()
        }

    }

}