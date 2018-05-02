package com.selasarimaji.perpus.repository.firebase

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.selasarimaji.perpus.model.DataModel

abstract class BaseRepo <T:DataModel>{
    protected abstract val collectionName : String

    abstract val fetchedData : MutableLiveData<List<T>>

    private val db by lazy {
        FirebaseFirestore.getInstance().collection(collectionName)
    }

    open fun loadRange(startPosition: Int, loadCount: Int, orderBy: String = "name",
                           listener : (querySnapshot:QuerySnapshot?, exception:FirebaseFirestoreException?) -> Unit){
        db.orderBy(orderBy).startAt(startPosition).limit(loadCount.toLong())
                .addSnapshotListener { querySnapshot, exception ->
                    listener(querySnapshot, exception)
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

    fun addLocalItem(dataModel: T){
        val items = listOf(dataModel)
        fetchedData.value?.toMutableList()?.run {
            this.addAll(items)
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

    fun editLocalItem(dataModel: T){
        fetchedData.value?.toMutableList()?.run {
            val position = indexOfFirst { it.id == dataModel.id }
            this[position] = dataModel
            fetchedData.value = this
        }
    }
}