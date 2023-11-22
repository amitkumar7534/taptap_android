package com.example.myapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.easyjob.ui.chat.AdapterMessages
import com.example.myapplication.chat.ChatFragment
import com.example.myapplication.fragments.FollowRequestFragment
import com.example.myapplication.ui.base.BindingActivity
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.ActivityCommonBinding
import com.yookoo.discoveraddis.databinding.ActivityMessagesBinding

class CommonActivity : BindingActivity<ActivityCommonBinding>() {
    var b = Bundle()



    override fun getLayout(): Int {
        return R.layout.activity_common
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = intent.extras!!
        val fragmentName = b.getString(getString(R.string.fragmentName))


        if (fragmentName != null && fragmentName.equals(
                getString(R.string.chat_fragment),
                ignoreCase = true)) {
            replaceFragment(ChatFragment())

        }else if (fragmentName != null && fragmentName.equals(
                getString(R.string.setting_fragment),
                ignoreCase = true)) {
            replaceFragment(SettingActivity())

        }else if (fragmentName != null && fragmentName.equals(
                getString(R.string.follow_fragment),
                ignoreCase = true)) {
            replaceFragment(FollowRequestFragment())

        }

    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()

    }

}