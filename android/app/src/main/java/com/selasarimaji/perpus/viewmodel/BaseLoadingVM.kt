package com.selasarimaji.perpus.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

abstract class BaseLoadingVM : ViewModel(){
    val isLoading = MutableLiveData<Boolean>()
    val shouldFinish = MutableLiveData<Boolean>()
}