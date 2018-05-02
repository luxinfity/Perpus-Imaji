package com.selasarimaji.perpus.repository.firestore

import android.arch.lifecycle.MutableLiveData
import com.selasarimaji.perpus.model.DataModel

class BorrowRepo : BaseRepo<DataModel.Borrow>() {
    override val collectionName: String
        get() = "Borrow"

    private val liveData by lazy {
        MutableLiveData<List<DataModel.Borrow>>()
    }

    override val fetchedData: MutableLiveData<List<DataModel.Borrow>>
        get() = liveData
}