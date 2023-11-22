package com.example.myapplication.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.adapter.CarAdapter
import com.example.myapplication.login.LoginActivity
import com.yookoo.discoveraddis.databinding.ActivitySettingBinding
import com.example.myapplication.ui.base.BaseFragment
import com.example.myapplication.ui.callback.ViewClickCallback
import com.example.myapplication.utils.Constant
import com.yookoo.discoveraddis.R

class SettingActivity : BaseFragment<ActivitySettingBinding>() {

    override fun getLayout(): Int {
        return R.layout.activity_setting
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tBinding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        tBinding.viewLogout.setOnClickListener {
            AlertDialog.Builder(requireActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Logout !")
                .setMessage("Are you sure you want to logout on this app?")
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, which ->
                        appSession().save("login_value", false)
                        val intent = Intent(requireActivity(), LoginActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finishActivity()
                    })
                .setNegativeButton("No", null)
                .show()
        }

        tBinding.deleteAccount.setOnClickListener {
            AlertDialog.Builder(requireActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Delete Account !")
                .setMessage("Are you sure you want to delete this account?")
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, which ->

                        showProgress()
                        Delete_Account()
                    })
                .setNegativeButton("No", null)
                .show()

        }
        tBinding.addCarView.setOnClickListener {
            val intent = Intent(requireActivity(), AddCarActivity::class.java)
            startActivity(intent)
        }
        tBinding.addNotificationView.setOnClickListener {
            val intent = Intent(requireActivity(), AddNotificationActivity::class.java)
            startActivity(intent)
        }
        tBinding.myAccount.setOnClickListener {
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            startActivity(intent)
        }
        tBinding.myBusienessView.setOnClickListener {
            val intent = Intent(requireActivity(), MyBussinessActivity::class.java)
            startActivity(intent)
        }
        tBinding.myCarView.setOnClickListener {
            val intent = Intent(requireActivity(), MyCarActivity::class.java)
            startActivity(intent)
        }
        tBinding.privacyPolicy.setOnClickListener {
            val intent = Intent(requireActivity(), PrivacyAndTerms::class.java)
            intent.putExtra("type", "privacy")
            startActivity(intent)
        }
        tBinding.termsView.setOnClickListener {
            val intent = Intent(requireActivity(), PrivacyAndTerms::class.java)
            intent.putExtra("type", "terms")
            startActivity(intent)
        }
        tBinding.appInfoView.setOnClickListener {
            val intent = Intent(requireActivity(), PrivacyAndTerms::class.java)
            intent.putExtra("type", "app_info")
            startActivity(intent)
        }
        tBinding.fAndQ.setOnClickListener {
            val intent = Intent(requireActivity(), PrivacyAndTerms::class.java)
            intent.putExtra("type", "fAndQ")
            startActivity(intent)
        }


    }

    fun Delete_Account() {
        getBaseActivity().baseViewModel.repository.delete_account(
            apiListener(),
            appSession().getString(Constant.USER_ID).toString())
            .observe(requireActivity()) {
                hideProgress()
                appSession().save("login_value", false)
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finishActivity()
            }
    }


}