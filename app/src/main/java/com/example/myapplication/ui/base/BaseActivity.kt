package com.example.myapplication.ui.base

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.activities.MainActivity
import com.yookoo.discoveraddis.R
import com.example.myapplication.app.App
import com.yookoo.discoveraddis.databinding.ProgressDialogBinding
import com.example.myapplication.session.Session
import com.example.myapplication.ui.callback.ApiListener
import com.example.myapplication.ui.factory.ViewModelProvideFactory

abstract class BaseActivity : AppCompatActivity(), BaseCallback {
    lateinit var activity: Activity
    lateinit var baseViewModel: BaseViewModel
    var logoutDialog: Dialog? = null
    var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseViewModel = BaseViewModel(context = this)
        activity = this

    }


    override fun showToast(message : String) {
       Toast.makeText(this,message,Toast.LENGTH_SHORT).show()


    }


    private fun logoutUser() {
        showProgress()
        Handler(Looper.getMainLooper()).postDelayed({
            hideProgress()
            appSession().logout()
           /* val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startNewActivity(intent) */   }, 2000)
    }


    private fun hideLogoutDialog() {
        logoutDialog?.dismiss()

        logoutDialog = null
    }


    override fun onStart() {
        super.onStart()
    }


    override fun onStop() {
        super.onStop()
    }


    override fun openforgotpass() {
        TODO("Not yet implemented")
    }

    override fun finishActivity() {
        finish()
    }

    override fun openHome() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        finish()

    }

    override fun showFragment(container: Int, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.add(container, fragment)
        transaction.commit()
    }

    override fun replaceFragment(container: Int, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.replace(container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }





    override fun getCurrentActivity(): Activity {
        return activity
    }

    @Throws(WindowManager.BadTokenException::class)
    override fun showProgress() {
        try {
            if (progressDialog == null) {
                progressDialog = Dialog(this)
                val progressDialogBinding: ProgressDialogBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(activity), R.layout.progress_dialog, null, false)
                progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                progressDialog!!.window!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setContentView(progressDialogBinding.root)
                progressDialog!!.window!!.setDimAmount(0.8f)
                progressDialog!!.show()
            }
        }catch (t: Throwable) {
            t.printStackTrace()
        }
    }
    fun <T : ViewModel> getViewModel(java: Class<T>): T {
        val factory = ViewModelProvideFactory(java)
        return ViewModelProvider(this, factory)[java]
    }

    override fun apiListener(): ApiListener {
        return apiListener
    }


    private val apiListener: ApiListener = object : ApiListener {
        override fun progress(isVisible: Boolean) {
            if (isVisible) showProgress()
            else hideProgress()
        }

        override fun msg(msg: String) {
            showToast(msg)
        }
    }

    override fun handleError(t: Throwable) {
        TODO("Not yet implemented")
    }

    override fun hideProgress() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
            progressDialog = null
        }

    }

    override fun appSession(): Session = App.getSharedInstance()


    override fun baseinit() {

    }

    override fun startNewActivity(intent: Intent) {
        startActivity(intent)
    }


    fun loadGlideInCircle(url: String, imageView: ImageView, resID: Int) {
        Glide.with(activity).load(url).placeholder(resID).apply(RequestOptions.circleCropTransform()).into(imageView)
    }

    fun loadGlide(url: String, imageView: ImageView, resID: Int) {
        Glide.with(activity).load(url).placeholder(resID).into(imageView)
    }







}