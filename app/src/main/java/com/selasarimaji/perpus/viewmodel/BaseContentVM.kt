package com.selasarimaji.perpus.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentReference
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.repository.firestore.BaseRepo

abstract class BaseContentVM <T: DataModel> : BaseLoadingVM() {

    abstract val TAG : String

    open val repo: BaseRepo<T>? = null

    var title = MutableLiveData<String>()
    var totalRemoteCount = MutableLiveData<Int>()
    protected var isInitialLoaded = MutableLiveData<Boolean>()
    protected var lastIndex = MutableLiveData<Int>()
    protected var isContentLoading = MutableLiveData<Boolean>()
    protected var documentResultRef = MutableLiveData<DocumentReference>()

    open fun storeData(category: T){
        repo?.storeNewRemoteData(category, uploadingFlag, uploadingSuccessFlag, documentResultRef)
    }

    open fun reload(){
        isInitialLoaded.value = null
        repo?.clearLocalData()
        loadInitial()
    }

    open fun loadInitial(){
        repo?.getRemoteTotalCount { documentSnapshot ->
            if (documentSnapshot.contains("count")){
                totalRemoteCount.value = documentSnapshot["count"].toString().toInt()
            }else{
                totalRemoteCount.value = 0
            }
        }
    }

    abstract fun loadMore()
}