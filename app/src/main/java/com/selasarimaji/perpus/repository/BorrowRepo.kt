package com.selasarimaji.perpus.repository

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.QuerySnapshot
import com.selasarimaji.perpus.model.RepoDataModel

class BorrowRepo : BaseRepo<RepoDataModel.Borrow>() {
    override val contentName: String
        get() = "borrow"

    private val liveData by lazy {
        MutableLiveData<List<RepoDataModel.Borrow>>()
    }

    override val fetchedData: MutableLiveData<List<RepoDataModel.Borrow>>
        get() = liveData

    override fun onLoadCallback(querySnapshot: QuerySnapshot) {
        querySnapshot.documents.map {
            createLocalItem(RepoDataModel.Borrow.turnDocumentToObject(it))
        }
    }
}