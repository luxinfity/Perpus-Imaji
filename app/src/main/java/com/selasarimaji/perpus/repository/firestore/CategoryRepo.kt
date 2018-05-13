package com.selasarimaji.perpus.repository.firestore

import android.arch.lifecycle.MutableLiveData
import com.selasarimaji.perpus.model.DataModel

class CategoryRepo : BaseRepo<DataModel.Category>() {
    override val collectionName: String
        get() = "content/category/list"

    private val liveData by lazy {
        MutableLiveData<List<DataModel.Category>>()
    }

    override val fetchedData: MutableLiveData<List<DataModel.Category>>
        get() = liveData
}