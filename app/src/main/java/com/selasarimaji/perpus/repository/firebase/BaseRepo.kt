package com.selasarimaji.perpus.repository.firebase

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PositionalDataSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.selasarimaji.perpus.model.DataModel

abstract class BaseRepo <T:DataModel> : PositionalDataSource <T>() {
    protected abstract val collectionName : String
    protected abstract val TAG : String

    val db by lazy {
        FirebaseFirestore.getInstance().collection(collectionName)
    }

    abstract fun loadRangeInternal(startPosition: Int, loadCount: Int, isInitial:Boolean, listener: Any)

    fun storeNewData(dataModel: T, loadingFlag: MutableLiveData<Boolean>,
                     successFlag: MutableLiveData<Boolean>){
        loadingFlag.value = true
        db.add(dataModel).addOnCompleteListener {
            loadingFlag.value = false
            successFlag.value = it.isSuccessful
        }
    }

    fun updateData(dataModel: T){
        db.document(dataModel.id!!).set(dataModel, SetOptions.merge())
    }
}