package com.selasarimaji.perpus.repository.firestore

import android.arch.lifecycle.MutableLiveData
import com.selasarimaji.perpus.model.DataModel

class BookRepo : BaseRepo<DataModel.Book>() {

    override val collectionName: String
        get() = "content/book/list"

    private val liveData by lazy {
        MutableLiveData<List<DataModel.Book>>()
    }

    override val fetchedData: MutableLiveData<List<DataModel.Book>>
        get() = liveData
}