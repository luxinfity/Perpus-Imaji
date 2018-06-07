package com.selasarimaji.perpus.repository

import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import com.google.firebase.firestore.*
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage
import com.selasarimaji.perpus.model.RepoDataModel
import com.selasarimaji.perpus.model.LoadingProcess
import com.selasarimaji.perpus.model.LoadingType
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
                    listener(it.result)
                }
                .addOnFailureListener {
                    listener(null)
                }
    }
    open fun loadFromRemote(startPosition: Int = -1, loadDistance: Int = -1,
                            orderBy: String = "name",
                            filterMap: Map<String, String>? = null,
                            loadingFlag: MutableLiveData<LoadingProcess>? = null){
        loadingFlag?.value = LoadingProcess(true, false, LoadingType.Read)
        if (filterMap != null && filterMap.size > 1){
            functions.getHttpsCallable("directCall-getContentWithCustomFilter")
                    .call(mapOf(
                            "contentType" to contentName,
                            "filter" to filterMap
                    ))
                    // TODO implement result mapping
                    .addOnCompleteListener {
                        val test = it.result.data
                        val res = test.toString()
                        loadingFlag?.value = LoadingProcess(false,
                                it.isSuccessful,
                                LoadingType.Read)
                    }
        } else {
            db.orderBy(orderBy).apply {
                if (startPosition > -1) startAt(startPosition)
                if (loadDistance > -1) limit(loadDistance.toLong())

                filterMap?.map {
                    this.whereGreaterThanOrEqualTo(it.key, it.value)
                }
            }.get().addOnCompleteListener {
                onLoadCallback(it.result)
                loadingFlag?.value = LoadingProcess(false, it.isSuccessful, LoadingType.Read)
            }
        }
    }

    abstract fun onLoadCallback(querySnapshot: QuerySnapshot)

    fun createRemoteData(dataModel: T,
                         loadingFlag: MutableLiveData<LoadingProcess>,
                         docRef: MutableLiveData<DocumentReference>){
        loadingFlag.value = LoadingProcess(true, false, LoadingType.Create)
        db.add(dataModel).addOnCompleteListener {
            docRef.value = it.result
            loadingFlag.value = LoadingProcess(false, it.isSuccessful, LoadingType.Create)
        }
    }
    fun deleteFromRemote(id: String, loadingFlag: MutableLiveData<LoadingProcess>){
        loadingFlag.value = LoadingProcess(true, false, LoadingType.Delete)
        db.document(id).delete().addOnCompleteListener {
            loadingFlag.value = LoadingProcess(false, it.isSuccessful, LoadingType.Delete)
        }
    }
    fun updateRemoteData(dataModel: T, loadingFlag: MutableLiveData<LoadingProcess>){
        loadingFlag.value = LoadingProcess(true, false, LoadingType.Update)
        db.document(dataModel.id).set(dataModel, SetOptions.merge()).addOnCompleteListener {
            loadingFlag.value = LoadingProcess(false, it.isSuccessful, LoadingType.Update)
        }
    }
    // endregion

    // region local data
    abstract val fetchedData : MutableLiveData<List<T>>
    fun clearLocalData(){
        fetchedData.value = fetchedData.value?.toMutableList().apply { this?.clear() }
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
                   loadingFlag: MutableLiveData<LoadingProcess>, completeListener: (Boolean) -> Unit){
        loadingFlag.value = LoadingProcess(true, false, LoadingType.Create)

        val file = Uri.fromFile(File(filePath))
        val remoteFile = storageRef.child("$docId.jpg")
        remoteFile.putFile(file)
                .addOnCompleteListener {
                    completeListener(it.isSuccessful)
                    loadingFlag.value = LoadingProcess(false, it.isSuccessful, LoadingType.Create)
                }
    }
    fun getImageFull(docId: String) = storageRef.child("$docId.jpg")
    fun getImageThumb(docId: String) = storageRef.child("thumb_$docId.jpg")
    // endregion
}