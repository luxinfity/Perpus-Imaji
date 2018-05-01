package com.selasarimaji.perpus.repository.firebase

import com.selasarimaji.perpus.model.DataModel

class KidRepo : BaseRepo<DataModel.Kid>() {
    override val collectionName: String
        get() = "Kid"

    override val TAG: String
        get() = KidRepo::javaClass.toString()

    override fun loadRangeInternal(startPosition: Int, loadCount: Int, isInitial: Boolean, listener: Any) {

    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<DataModel.Kid>) {

    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<DataModel.Kid>) {

    }
}