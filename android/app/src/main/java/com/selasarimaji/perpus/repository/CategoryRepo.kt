package com.selasarimaji.perpus.repository

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.QuerySnapshot
import com.selasarimaji.perpus.model.RepoDataModel

class CategoryRepo : BaseRepo<RepoDataModel.Category>() {
    override val contentName: String
        get() = "category"

    private val liveData by lazy {
        MutableLiveData<List<RepoDataModel.Category>>()
    }

    override val fetchedData: MutableLiveData<List<RepoDataModel.Category>>
        get() = liveData

    override fun onLoadCallback(querySnapshot: QuerySnapshot) {
        querySnapshot.documents.map {
            createLocalItem(RepoDataModel.Category.turnDocumentToObject(it))
        }
    }
}