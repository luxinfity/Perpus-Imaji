package com.selasarimaji.perpus.viewmodel

import android.util.Log
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.repository.firestore.BaseRepo
import com.selasarimaji.perpus.repository.firestore.KidRepo

class EditKidVM : BaseContentVM<DataModel.Kid>() {
    private val repoVal by lazy {
        KidRepo()
    }
    override val repo: BaseRepo<DataModel.Kid>
        get() = repoVal

    override val TAG: String
        get() = EditKidVM::class.java.name

    override fun loadInitial(){
        super.loadInitial()
        if (isInitialLoaded.value == null || !isInitialLoaded.value!!){
            lastIndex.value = 0
            isInitialLoaded.value = true
            repo.loadRange(0, 10, listener = this@EditKidVM::handleFirebaseQueryCallback)
        }
    }

    override fun loadMore() {
        isLoading.value?.run {
            if (!this){
                isLoading.value = true
                repo.loadRange(lastIndex.value!!, 10, listener = this@EditKidVM::handleFirebaseQueryCallback)
            }
        }
    }

    private fun handleFirebaseQueryCallback(querySnapshot: QuerySnapshot){
        querySnapshot.documents.map {
            repo.addLocalItem(DataModel.Kid.turnDocumentToObject(it))
        }
        lastIndex.value = lastIndex.value!! + 10
        isLoading.value = false
    }
}
