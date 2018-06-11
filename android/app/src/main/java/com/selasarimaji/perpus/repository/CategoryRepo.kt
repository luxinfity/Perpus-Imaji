package com.selasarimaji.perpus.repository

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.JsonArray
import com.selasarimaji.perpus.model.RepoDataModel

class CategoryRepo : BaseRepo<RepoDataModel.Category>() {
    override val contentName: String
        get() = "category"

    private val liveData by lazy {
        MutableLiveData<List<RepoDataModel.Category>>()
    }

    override val fetchedData: MutableLiveData<List<RepoDataModel.Category>>
        get() = liveData


    // region fetched data callback,
    // this two will be called 2 times on first call due to load initial and load more
    override fun onLoadCallback(querySnapshot: QuerySnapshot?) {
        querySnapshot?.documents?.map {
            createLocalItem(RepoDataModel.Category.turnDocumentToObject(it))
        }
    }

    override fun onLoadCallback(jsonArray: JsonArray?) {
        jsonArray?.map {
            val data = it.asJsonObject
            createLocalItem(RepoDataModel.Category.turnDocumentToObject(data))
        }
    }
    // endregion
}