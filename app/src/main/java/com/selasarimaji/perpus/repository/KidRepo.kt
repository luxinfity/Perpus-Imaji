package com.selasarimaji.perpus.repository

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.QuerySnapshot
import com.selasarimaji.perpus.model.RepoDataModel

class KidRepo : BaseRepo<RepoDataModel.Kid>() {
    override val contentName: String
        get() = "kid"

    private val liveData by lazy {
        MutableLiveData<List<RepoDataModel.Kid>>()
    }

    override val fetchedData: MutableLiveData<List<RepoDataModel.Kid>>
        get() = liveData

    override fun onLoadCallback(querySnapshot: QuerySnapshot) {
        querySnapshot.documents.map {
            createLocalItem(RepoDataModel.Kid.turnDocumentToObject(it))
        }
    }
}