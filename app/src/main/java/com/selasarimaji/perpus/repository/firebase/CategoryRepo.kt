package com.selasarimaji.perpus.repository.firebase

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.selasarimaji.perpus.model.Model


class CategoryRepo {
    companion object {
        const val COLLECTION_NAME = "Category"
    }

    private val db = FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
    fun storeNewData(category: Model.Category, loadingFlag: MutableLiveData<Boolean>,
                     successFlag: MutableLiveData<Boolean>){
        loadingFlag.value = true
        db.add(category).addOnCompleteListener {
            loadingFlag.value = false
            successFlag.value = it.isSuccessful
        }
    }

    fun updateData(category: Model.Category){
        db.document(category.id!!).set(category, SetOptions.merge())
    }
}