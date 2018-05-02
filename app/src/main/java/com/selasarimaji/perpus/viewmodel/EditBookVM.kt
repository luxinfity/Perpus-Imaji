package com.selasarimaji.perpus.viewmodel

import android.util.Log
import com.google.firebase.firestore.DocumentChange
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.repository.firebase.BaseRepo
import com.selasarimaji.perpus.repository.firebase.BookRepo

class EditBookVM : BaseContentVM<DataModel.Book>() {
    override val TAG: String
        get() = EditBookVM::class.java.name

    private val repoVal by lazy {
        BookRepo()
    }
    override val repo: BaseRepo<DataModel.Book>
        get() = repoVal

    override fun loadInitial(){
        repo.loadRange(0, 10){ querySnapshot, exception ->
            if (exception != null) {
                Log.w(TAG, "listen:error", exception)
            }else {
                querySnapshot!!.documentChanges.map {
                    when(it.type){
                        DocumentChange.Type.ADDED -> repo.addLocalItem(DataModel.Book.turnDocumentToObject(it.document))
                        DocumentChange.Type.MODIFIED -> repo.editLocalItem(DataModel.Book.turnDocumentToObject(it.document))
                        DocumentChange.Type.REMOVED -> repo.deleteLocalItem(DataModel.Book.turnDocumentToObject(it.document))
                    }
                }
            }
        }
    }
}