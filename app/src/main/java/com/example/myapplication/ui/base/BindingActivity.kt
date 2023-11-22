package com.example.myapplication.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

abstract class BindingActivity<TBinding : ViewDataBinding> : BaseActivity() {

    @LayoutRes
    protected abstract fun getLayout(): Int
    lateinit var viewBinding: TBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this@BindingActivity
        initBinding()
    }

    private fun initBinding() {
        baseViewModel = BaseViewModel(activity)
        viewBinding = DataBindingUtil.setContentView(activity, getLayout())
        //updateFcmToken()
    }

    /**
     * Creates a [ViewModel] and binds it to the [ViewDataBinding] for this view.
     */
    protected inline fun <reified T : ViewModel> bindViewModel(
        noinline f: (TBinding.(T) -> Unit)? = null
    ): T {
        val viewModel = getViewModel(T::class.java)
        f?.invoke(viewBinding, viewModel)
        viewBinding.executePendingBindings()
        return viewModel
    }


    /*private fun updateFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("ATAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            Log.d("ATAG", token)
            if (appSession().isLogin()) {
                baseViewModel.updateToken(token)
            }
//            apiRepository.updateToken(token)
        })
    }*/

}