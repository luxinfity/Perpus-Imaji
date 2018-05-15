package com.selasarimaji.perpus.repository.firestore

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.*
import com.selasarimaji.perpus.model.DataModel

abstract class BaseRepo <T:DataModel>{
    protected abstract val collectionName : String

    abstract val fetchedData : MutableLiveData<List<T>>

    private val db by lazy {
        FirebaseFirestore.getInstance().collection(collectionName)
    }

    fun getRemoteTotalCount(listener : (documentSnapshot: DocumentSnapshot) -> Unit){
        db.document(collectionName).get().addOnSuccessListener {
            listener(it)
        }
    }

    fun clearLocalData(){
        fetchedData.value = fetchedData.value?.toMutableList().apply { this?.clear() }
    }

    open fun loadRange(startPosition: Int, loadCount: Int, orderBy: String = "name",
                           listener : (querySnapshot:QuerySnapshot) -> Unit){
        db.orderBy(orderBy).startAt(startPosition).limit(loadCount.toLong())
                .get().addOnSuccessListener {
                    listener(it)
                }
    }

    fun storeNewRemoteData(dataModel: T, loadingFlag: MutableLiveData<Boolean>,
                           successFlag: MutableLiveData<Boolean>){
        loadingFlag.value = true
        db.add(dataModel).addOnCompleteListener {
            loadingFlag.value = false
            successFlag.value = it.isSuccessful
        }
    }

    fun updateRemoteData(dataModel: T){
        db.document(dataModel.id!!).set(dataModel, SetOptions.merge())
    }

    open fun addLocalItem(dataModel: T){
        if (editLocalItem(dataModel)) return // don't need to add if edit value

        val items = mutableListOf(dataModel)
        fetchedData.value?.run {
            items.addAll(0,this)
        }
        fetchedData.value = items
    }

    fun deleteLocalItem(dataModel: T){
        fetchedData.value?.toMutableList()?.run {
            val position = indexOfFirst { it.id == dataModel.id }
            if (position > -1){
                this.removeAt(position)
            }
            fetchedData.value = this
        }
    }

    fun editLocalItem(dataModel: T) : Boolean{
        fetchedData.value?.toMutableList()?.run {
            val position = indexOfFirst { it.id == dataModel.id }
            if (position > -1) {
                this[position] = dataModel
                fetchedData.value = this
                return true
            }
        }
        return false
    }

    fun getContentWith(field: String, query: String, listener : (querySnapshot:QuerySnapshot, query: String) -> Unit){
        db.orderBy(field).whereGreaterThanOrEqualTo(field, query).get().addOnSuccessListener {
            listener(it, query)
        }
    }
}