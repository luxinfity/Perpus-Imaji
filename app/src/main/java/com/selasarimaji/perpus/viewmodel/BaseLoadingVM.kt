package com.selasarimaji.perpus.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

abstract class BaseLoadingVM : ViewModel(){
    var uploadingFlag = MutableLiveData<Boolean>().apply {
        value = false
    }
    var uploadingSuccessFlag = MutableLiveData<Boolean>().apply {
        value = false
    }
}