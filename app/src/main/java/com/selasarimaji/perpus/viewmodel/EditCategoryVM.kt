package com.selasarimaji.perpus.viewmodel

import android.util.Log
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.repository.firestore.BaseRepo
import com.selasarimaji.perpus.repository.firestore.CategoryRepo

class EditCategoryVM : BaseContentVM<DataModel.Category>() {
    private val repoVal by lazy {
        CategoryRepo()
    }
    override val repo: BaseRepo<DataModel.Category>
        get() = repoVal

    override val TAG: String
        get() = EditCategoryVM::class.java.name

    override fun loadInitial(){
        if (isInitialLoaded.value == null || !isInitialLoaded.value!!){
            lastIndex.value = 0
            repo.loadRange(0, 10, listener = this@EditCategoryVM::handleFirebaseQueryCallback)
        }
    }

    override fun loadMore() {
        isLoading.value?.run {
            if (!this){
                isLoading.value = true
                repo.loadRange(lastIndex.value!!, 10, listener = this@EditCategoryVM::handleFirebaseQueryCallback)
            }
        }
    }

    private fun handleFirebaseQueryCallback(querySnapshot: QuerySnapshot?, exception: FirebaseFirestoreException?){
        if (exception != null) {
            Log.w(TAG, "listen:error", exception)
        } else {
            querySnapshot!!.documentChanges.map {
                when (it.type) {
                    DocumentChange.Type.ADDED -> repo.addLocalItem(DataModel.Category.turnDocumentToObject(it.document))
                    DocumentChange.Type.MODIFIED -> repo.editLocalItem(DataModel.Category.turnDocumentToObject(it.document))
                    DocumentChange.Type.REMOVED -> repo.deleteLocalItem(DataModel.Category.turnDocumentToObject(it.document))
                }
            }
            lastIndex.value = lastIndex.value!! + 10
        }
        isLoading.value = false
    }
}