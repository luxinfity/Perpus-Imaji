package com.selasarimaji.perpus.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.selasarimaji.perpus.model.LoadingProcess

abstract class BaseLoadingVM : ViewModel(){
    val loadingProcess = MutableLiveData<LoadingProcess>()
}