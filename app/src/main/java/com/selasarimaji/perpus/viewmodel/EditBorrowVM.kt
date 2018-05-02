package com.selasarimaji.perpus.viewmodel

import android.util.Log
import com.google.firebase.firestore.DocumentChange
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

    override fun loadInitial() {
        if (isInitialLoaded.value == null || !isInitialLoaded.value!!){
            repo.loadRange(0, 10, "startDate") { querySnapshot, exception ->
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
                }
            }
            isInitialLoaded.value = true
        }
    }
}