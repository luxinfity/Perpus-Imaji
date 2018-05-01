package com.selasarimaji.perpus.repository.firebase

import com.selasarimaji.perpus.model.DataModel

class BorrowRepo : BaseRepo<DataModel.Borrow>() {
    override val collectionName: String
        get() = "Borrow"

    override val TAG: String
        get() = BorrowRepo::javaClass.toString()

    override fun loadRangeInternal(startPosition: Int, loadCount: Int, isInitial: Boolean, listener: Any) {

    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<DataModel.Borrow>) {

    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<DataModel.Borrow>) {

    }
}