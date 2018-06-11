package com.selasarimaji.perpus.viewmodel

import com.selasarimaji.perpus.model.Loading
import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.JsonArray
import com.selasarimaji.perpus.model.RepoDataModel
import com.selasarimaji.perpus.repository.BaseRepo

abstract class BaseContentCreationVM <T: RepoDataModel> : BaseLoadingVM() {
    abstract val repo: BaseRepo<T>
    open val loadDistance = 10
    var documentResultRef: String? = null

    var title = MutableLiveData<String>()
    var totalRemoteCount = MutableLiveData<Int>()
    var filterMap: Map<String, String>? = null

    open fun getRealNameOfId(id: String, onResult: (String) -> Unit){
        if (id.isNotEmpty()) {
            repo.getRealNameOfId(id) {
                it?.let(onResult)
            }
        }
    }

    open fun storeData(dataModel: T, onResult: (Loading.Result<String>) -> Unit){
        repo.createRemoteData(dataModel, isLoading, onResult)
    }

    open fun updateData(dataModel: T, onResult: (Loading.Result<Any>) -> Unit){
        repo.updateRemoteData(dataModel, isLoading, onResult)
    }

    open fun reload(){
        repo.clearLocalData()
        filterMap = null
        loadInitial(filterMap)
    }

    fun getTotalRemoteCount(){
        repo.getRemoteTotalCount {
            totalRemoteCount.value = it
        }
    }

    open fun loadInitial(filterMap: Map<String, String>? = null){
        getTotalRemoteCount()
        isLoading.value.let {
            if (it != true){
                repo.loadFromRemote(Loading.Param(
                        Loading.Param.Position(0, loadDistance), filterMap = filterMap),
                        isLoading){
                    if (it.data is QuerySnapshot) repo.onLoadCallback(it.data)
                    else if (it.data is JsonArray) repo.onLoadCallback(it.data)
                }
            }
        }
    }

    open fun loadMore(filterMap: Map<String, String>? = null){
        isLoading.value.let {
            if (it != true){
                repo.loadFromRemote(Loading.Param(
                        Loading.Param.Position(repo.fetchedData.value!!.size, loadDistance), filterMap = filterMap),
                        isLoading){
                    if (it.data is QuerySnapshot) repo.onLoadCallback(it.data)
                    else if (it.data is JsonArray) repo.onLoadCallback(it.data)
                }
            }
        }
    }

    fun deleteCurrent(item: RepoDataModel, onResult: (Loading.Result<Any>) -> Unit){
        isLoading.value.let {
            if (it != true){
                repo.deleteFromRemote(item.id, isLoading, onResult)
            }
        }
    }

    fun canSafelyDeleted(id: String, result: (Loading.Result<Boolean>) -> Unit) {
        isLoading.value.let {
            if (it != true){
                repo.canSafelyDelete(id, isLoading, result)
            }
        }
    }
}