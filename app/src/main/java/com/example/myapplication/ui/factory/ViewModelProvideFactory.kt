package com.example.myapplication.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.jvm.Throws


class ViewModelProvideFactory<T2 : ViewModel>(var viewModel: Class<T2>) : ViewModelProvider.Factory {


    @Throws(IllegalArgumentException::class)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(viewModel)) {
            return viewModel as T
        }
        throw  IllegalArgumentException("Unknown class name");

    }


}



