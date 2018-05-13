package com.selasarimaji.perpus.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.repository.firestore.BaseRepo

abstract class BaseContentVM <T: DataModel> : BaseLoadingVM() {

    abstract val TAG : String

    open val repo: BaseRepo<T>? = null

    var title = MutableLiveData<String>()
    var totalRemoteCount = MutableLiveData<Int>()
    protected var isInitialLoaded = MutableLiveData<Boolean>()
    protected var lastIndex = MutableLiveData<Int>()
    protected var isLoading = MutableLiveData<Boolean>()

    open fun storeData(category: T){
        repo?.storeNewRemoteData(category, uploadingFlag, uploadingSuccessFlag)
    }

    open fun loadInitial(){
        repo?.getRemoteTotalCount { documentSnapshot, _ ->
            documentSnapshot?.let {
                if (it.contains("count")){
                    totalRemoteCount.value = it["count"].toString().toInt()
                }else{
                    totalRemoteCount.value = 0
                }
            }
        }
    }

    abstract fun loadMore()
}