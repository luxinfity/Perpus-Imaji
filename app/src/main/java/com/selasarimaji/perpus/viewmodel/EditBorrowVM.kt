package com.selasarimaji.perpus.viewmodel

import android.util.Log
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.repository.firestore.BaseRepo
import com.selasarimaji.perpus.repository.firestore.BorrowRepo

class EditBorrowVM : BaseContentVM<DataModel.Borrow>() {

    private val repoVal by lazy {
        BorrowRepo()
    }
    override val repo: BaseRepo<DataModel.Borrow>
        get() = repoVal

    override val TAG: String
        get() = EditBorrowVM::class.java.name

    override fun loadInitial(){
        if (isInitialLoaded.value == null || !isInitialLoaded.value!!){
            lastIndex.value = 0
            repo.loadRange(0, 10, listener = this@EditBorrowVM::handleFirebaseQueryCallback)
        }
    }

    override fun loadMore() {
        isLoading.value?.run {
            if (!this){
                isLoading.value = true
                repo.loadRange(lastIndex.value!!, 10, listener = this@EditBorrowVM::handleFirebaseQueryCallback)
            }
        }
    }

    private fun handleFirebaseQueryCallback(querySnapshot: QuerySnapshot?, exception: FirebaseFirestoreException?){
        if (exception != null) {
            Log.w(TAG, "listen:error", exception)
        } else {
            querySnapshot!!.documentChanges.map {
                when (it.type) {
                    DocumentChange.Type.ADDED -> repo.addLocalItem(DataModel.Borrow.turnDocumentToObject(it.document))
                    DocumentChange.Type.MODIFIED -> repo.editLocalItem(DataModel.Borrow.turnDocumentToObject(it.document))
                    DocumentChange.Type.REMOVED -> repo.deleteLocalItem(DataModel.Borrow.turnDocumentToObject(it.document))
                }
            }
            lastIndex.value = lastIndex.value!! + 10
        }
        isLoading.value = false
    }
}