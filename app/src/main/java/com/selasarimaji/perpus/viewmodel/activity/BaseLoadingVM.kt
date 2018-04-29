package com.selasarimaji.perpus.viewmodel.activity

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

abstract class BaseLoadingVM : ViewModel(){
    var uploadingFlag : MutableLiveData<Boolean> = MutableLiveData()
    var uploadingSuccessFlag : MutableLiveData<Boolean> = MutableLiveData()

    fun initLiveData(){
        uploadingFlag.value = false
        uploadingSuccessFlag.value = false
    }
}