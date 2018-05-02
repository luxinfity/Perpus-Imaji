package com.selasarimaji.perpus.viewmodel

import android.util.Log
import com.google.firebase.firestore.DocumentChange
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.repository.firebase.BaseRepo
import com.selasarimaji.perpus.repository.firebase.CategoryRepo

class EditCategoryVM : BaseContentVM<DataModel.Category>() {
    private val repoVal by lazy {
        CategoryRepo()
    }
    override val repo: BaseRepo<DataModel.Category>
        get() = repoVal

    override val TAG: String
        get() = EditCategoryVM::class.java.name

    override fun loadInitial() {
        repo.loadRange(0, 10){ querySnapshot, exception ->
            if (exception != null) {
                Log.w(TAG, "listen:error", exception)
            }else {
                querySnapshot!!.documentChanges.map {
                    when(it.type){
                        DocumentChange.Type.ADDED -> repo.addLocalItem(DataModel.Category.turnDocumentToObject(it.document))
                        DocumentChange.Type.MODIFIED -> repo.editLocalItem(DataModel.Category.turnDocumentToObject(it.document))
                        DocumentChange.Type.REMOVED -> repo.deleteLocalItem(DataModel.Category.turnDocumentToObject(it.document))
                    }
                }
            }
        }
    }
}