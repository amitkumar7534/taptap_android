package com.example.myapplication.ui.base

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.myapplication.api.repo.ApiRepository

open class BaseViewModel(var context: Context) : ViewModel() {

    var repository: ApiRepository = ApiRepository(context)






}