package com.selasarimaji.perpus.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.repository.firebase.BaseRepo

abstract class BaseContentVM <T: DataModel> : BaseLoadingVM() {

    open val repo: BaseRepo<T>? = null

    var title = MutableLiveData<String>()

    open fun storeData(category: T){
        repo?.storeNewData(category, uploadingFlag, uploadingSuccessFlag)
    }
}