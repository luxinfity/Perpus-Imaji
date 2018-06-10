package com.selasarimaji.perpus.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentReference
import com.selasarimaji.perpus.model.RepoDataModel
import com.selasarimaji.perpus.repository.BaseRepo

abstract class BaseContentCreationVM <T: RepoDataModel> : BaseLoadingVM() {
    abstract val repo: BaseRepo<T>
    open val loadDistance = 10

    var title = MutableLiveData<String>()
    var totalRemoteCount = MutableLiveData<Int>()
    var documentResultRef = MutableLiveData<String>()

    open fun storeData(dataModel: T){
        repo.createRemoteData(dataModel, loadingProcess, documentResultRef)
    }

    open fun updateData(dataModel: T){
        repo.updateRemoteData(dataModel, loadingProcess)
    }

    open fun reload(filterMap: Map<String, String>? = null){
        repo.clearLocalData()
        loadInitial(filterMap)
    }

    fun getTotalRemoteCount(){
        repo.getRemoteTotalCount {
            totalRemoteCount.value = it
        }
    }

    open fun loadInitial(filterMap: Map<String, String>? = null){
        getTotalRemoteCount()
        loadingProcess.value.let {
            if (it?.isLoading != true){
                repo.loadFromRemote(0,
                        loadDistance,
                        filterMap = filterMap,
                        loadingFlag = loadingProcess)
            }
        }
    }

    open fun loadMore(filterMap: Map<String, String>? = null){
        loadingProcess.value.let {
            if (it?.isLoading != true){
                repo.loadFromRemote(repo.fetchedData.value!!.size,
                        loadDistance,
                        filterMap = filterMap,
                        loadingFlag = loadingProcess)
            }
        }
    }

    fun deleteCurrent(item: RepoDataModel){
        loadingProcess.value.let {
            if (it?.isLoading != true) {
                repo.deleteFromRemote(item.id, loadingProcess)
            }
        }
    }
}