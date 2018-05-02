package com.selasarimaji.perpus.repository.firestore

import android.arch.lifecycle.MutableLiveData
import com.selasarimaji.perpus.model.DataModel

class KidRepo : BaseRepo<DataModel.Kid>() {
    override val collectionName: String
        get() = "Kid"

    private val liveData by lazy {
        MutableLiveData<List<DataModel.Kid>>()
    }

    override val fetchedData: MutableLiveData<List<DataModel.Kid>>
        get() = liveData
}