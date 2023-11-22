package com.example.myapplication.ui.base

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.activities.MainActivity
import com.yookoo.discoveraddis.R
import com.example.myapplication.app.App
import com.yookoo.discoveraddis.databinding.ProgressDialogBinding
import com.example.myapplication.session.Session
import com.example.myapplication.ui.callback.ApiListener

abstract class BaseFragment<T:ViewBinding> : Fragment(), BaseCallback {
     lateinit var tBinding : T
    var logoutDialog: Dialog? = null
    var progressDialog: Dialog? = null

     @LayoutRes
     abstract fun getLayout() : Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         tBinding = DataBindingUtil.inflate(inflater,getLayout(),container,false)

        return tBinding.root
    }



    open fun getBaseActivity(): BaseActivity {
        val activity = activity
        if (activity is BaseActivity) {
            return activity
        }
        throw RuntimeException("BaseActivity is null")
    }

    override fun showFragment(container: Int, fragment: Fragment) {
       getBaseActivity().showFragment(container,fragment)
    }

    override fun replaceFragment(container: Int, fragment: Fragment) {
   getBaseActivity().replaceFragment(container,fragment)
    }

    override fun showToast(message: String) {
        if (activity!=null) {
                Toast.makeText(getBaseActivity(), message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun openforgotpass() {
        TODO("Not yet implemented")
    }

    override fun finishActivity() {
        getBaseActivity().finish()
    }

    override fun openHome() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        getBaseActivity().finish()

    }

    override fun getCurrentActivity(): Activity {
        return requireActivity()
    }

    override fun showProgress() {
        try {
            if (progressDialog == null) {
                progressDialog = Dialog(getBaseActivity())
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

    override fun apiListener(): ApiListener {
        return apiListener
    }


    fun replaceFragmentActivityWithoutStack(
        fragmentManager: FragmentManager?,
        fragment: Fragment, container: Int, ) {
        requireFragmentManager().beginTransaction()
            .replace(container, fragment)
            .commit()
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
        Glide.with(getBaseActivity()).load(url).placeholder(resID).apply(RequestOptions.circleCropTransform()).into(imageView)
    }

    fun loadGlide(url: String, imageView: ImageView, resID: Int) {
        Glide.with(getBaseActivity()).load(url).placeholder(resID).into(imageView)
    }






}