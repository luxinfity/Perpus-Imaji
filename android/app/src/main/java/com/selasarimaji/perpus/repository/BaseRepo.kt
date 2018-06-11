package com.selasarimaji.perpus.repository

import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import com.google.firebase.firestore.*
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.selasarimaji.perpus.model.*
import java.io.File

abstract class BaseRepo <T:RepoDataModel> {
    protected abstract val contentName: String

    private val collectionName by lazy {
        "content/$contentName/list"
    }
    private val storagePath by lazy {
        "content/$contentName"
    }
    private val db by lazy {
        FirebaseFirestore.getInstance().collection(collectionName)
    }
    private val functions by lazy {
        FirebaseFunctions.getInstance()
    }
    private val storageRef by lazy {
        FirebaseStorage.getInstance().getReference(storagePath)
    }

    // region remote data
    fun getRemoteTotalCount(listener : (Int?) -> Unit){
        functions.getHttpsCallable("directCall-getContentCount")
                .call(mapOf("contentType" to contentName))
                .continueWith {
                    it.result.data.toString().toInt()
                }
                .addOnCompleteListener {
                    try {
                        listener(it.result)
                    } catch (ex: Exception) {
                        listener(null)
                    }
                }
    }
    open fun loadFromRemote(params: Loading.Param = Loading.Param(),
                            loadingFlag: MutableLiveData<Boolean>? = null,
                            onResult: (Loading.Result<Any>) -> Unit ){
        loadingFlag?.value = true
        if (params.filterMap != null){
            functions.getHttpsCallable("directCall-getContentWithCustomFilter")
                    .call(mapOf(
                            "contentType" to contentName,
                            "filter" to params.filterMap
                    ))
                    .continueWith {
                        Gson().toJsonTree(it.result.data).asJsonArray
                    }
                    .addOnCompleteListener {
                        loadingFlag?.value = false
                        try {
                            onResult(Loading.Result(true, Loading.Type.Read, it.result))
                        } catch (ex: Exception){
                            onResult(Loading.Result(false, Loading.Type.Read))
                        }
                    }
        } else {
            db.orderBy(params.orderBy).apply {
                if (params.loadPosition.start > -1) startAt(params.loadPosition.start)
                if (params.loadPosition.stop > -1) limit(params.loadPosition.stop.toLong())

                params.filterMap?.map {
                    this.whereGreaterThanOrEqualTo(it.key, it.value)
                }
            }.get().addOnCompleteListener {
                loadingFlag?.value = false
                onResult(Loading.Result(it.isSuccessful, Loading.Type.Read, it.result))
            }
        }
    }

    abstract fun onLoadCallback(querySnapshot: QuerySnapshot?)
    abstract fun onLoadCallback(jsonArray: JsonArray?)

    fun createRemoteData(dataModel: T,
                         loadingFlag: MutableLiveData<Boolean>,
                         onResult: (Loading.Result<String>) -> Unit){
        loadingFlag.value = true
        db.add(dataModel).addOnCompleteListener {
            loadingFlag.value = false
            onResult(Loading.Result(it.isSuccessful, Loading.Type.Create, it.result.id))
        }
    }
    fun deleteFromRemote(id: String,
                         loadingFlag: MutableLiveData<Boolean>,
                         onResult: (Loading.Result<Any>) -> Unit){
        loadingFlag.value = true
        db.document(id).delete().addOnCompleteListener {
            loadingFlag.value = false
            onResult(Loading.Result(it.isSuccessful, Loading.Type.Delete))
        }
    }
    fun updateRemoteData(dataModel: T,
                         loadingFlag: MutableLiveData<Boolean>,
                         onResult: (Loading.Result<Any>) -> Unit){
        loadingFlag.value = true
        db.document(dataModel.id).set(dataModel, SetOptions.merge()).addOnCompleteListener {
            loadingFlag.value = false
            onResult(Loading.Result(it.isSuccessful, Loading.Type.Update))
        }
    }
    fun getRealNameOfId(id: String, onResult: (String?) -> Unit){
        db.document(id).get().addOnCompleteListener {
            if (it.isSuccessful) {
                onResult(it.result["name"].toString())
            } else {
                onResult(null)
            }
        }.addOnFailureListener {
            onResult(null)
        }
    }
    fun canSafelyDelete(id: String,
                        loadingFlag: MutableLiveData<Boolean>,
                        onResult: (Loading.Result<Boolean>) -> Unit){
        loadingFlag.value = true
        functions.getHttpsCallable("directCall-canItemSafelyDelete")
                .call(mapOf(
                        "contentType" to contentName,
                        "id" to id
                ))
                .addOnCompleteListener {
                    loadingFlag.value = false
                    try {
                        val result = it.result.data.toString().toBoolean()
                        onResult(Loading.Result(it.isSuccessful, Loading.Type.Delete, result))
                    } catch (ex: Exception) {
                        onResult(Loading.Result(false, Loading.Type.Delete))
                    }
                }
    }
    // endregion

    // region local data
    abstract val fetchedData : MutableLiveData<List<T>>
    fun clearLocalData(){
        fetchedData.value = fetchedData.value?.toMutableList()?.apply { clear() }
    }

    open fun createLocalItem(dataModel: T){
        if (editLocalItem(dataModel)) return // don't need to add if edit value

        fetchedData.value = mutableListOf(dataModel).also {
            fetchedData.value?.run {
                it.addAll(0,this)
            }
        }
    }

    fun deleteLocalItem(dataModel: T): Boolean{
        val pos = fetchedData.value?.indexOfFirst { it.id == dataModel.id } ?: -1

        return (pos > -1).also {
            val newDataList = fetchedData.value!!.toMutableList().also {
                it.removeAt(pos)
            }
            fetchedData.value = newDataList
        }
    }

    private fun editLocalItem(dataModel: T): Boolean{
        val pos = fetchedData.value?.indexOfFirst { it.id == dataModel.id } ?: -1

        return (pos > -1).also {
            if (!it) return false
            val newDataList = fetchedData.value?.toMutableList()?.also {
                it[pos] = dataModel
            }
            fetchedData.value = newDataList
        }
    }
    // endregion

    // region remote image
    fun storeImage(filePath: String,
                   docId: String,
                   loadingFlag: MutableLiveData<Boolean>,
                   onResult: (Loading.Result<Boolean>) -> Unit){

        loadingFlag.value = true

        val file = Uri.fromFile(File(filePath))
        val remoteFile = storageRef.child("$docId.jpg")
        remoteFile.putFile(file)
            .addOnCompleteListener {
                loadingFlag.value = false
                onResult(Loading.Result(it.isSuccessful, Loading.Type.Create))
            }
    }
    fun deleteImage(docId: String,
                    loadingFlag: MutableLiveData<Boolean>,
                    onResult: (Loading.Result<Boolean>) -> Unit){
        loadingFlag.value = true
        getImageThumb(docId).delete().addOnCompleteListener {
            getImageFull(docId).delete().addOnCompleteListener {
                loadingFlag.value = false
                onResult(Loading.Result(it.isSuccessful, Loading.Type.Delete))
            }
        }
    }
    fun getImageFull(docId: String) = storageRef.child("$docId.jpg")
    fun getImageThumb(docId: String) = storageRef.child("thumb_$docId.jpg")
    // endregion
}