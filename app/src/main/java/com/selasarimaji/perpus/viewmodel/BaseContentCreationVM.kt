package com.selasarimaji.perpus.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentReference
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.repository.firestore.BaseRepo

abstract class BaseContentCreationVM <T: DataModel> : BaseLoadingVM() {
    abstract val TAG : String
    abstract val repo: BaseRepo<T>
    open val loadCount = 10

    var title = MutableLiveData<String>()
    var totalRemoteCount = MutableLiveData<Int>()
    var contentCreationEnabled = MutableLiveData<Boolean>()
    protected var isInitialLoaded = MutableLiveData<Boolean>()
    protected var lastIndex = MutableLiveData<Int>()
    protected var isContentLoading = MutableLiveData<Boolean>()
    protected var documentResultRef = MutableLiveData<DocumentReference>()

    open fun storeData(category: T){
        repo.storeNewRemoteData(category, uploadingFlag, uploadingSuccessFlag, documentResultRef)
    }

    open fun reload(filterMap: Map<String, String>? = null){
        isInitialLoaded.value = null
        repo.clearLocalData()
        loadInitial(filterMap)
    }

    open fun loadInitial(filterMap: Map<String, String>? = null){
        repo.getRemoteTotalCount { documentSnapshot ->
            if (documentSnapshot.contains("count")){
                totalRemoteCount.value = documentSnapshot["count"].toString().toInt()
            }else{
                totalRemoteCount.value = 0
            }
        }
    }

    abstract fun loadMore(filterMap: Map<String, String>? = null)
}