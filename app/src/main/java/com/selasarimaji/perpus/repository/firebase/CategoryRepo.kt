package com.selasarimaji.perpus.repository.firebase

import com.selasarimaji.perpus.model.DataModel

class CategoryRepo : BaseRepo<DataModel.Category>() {
    override val collectionName: String
        get() = "Category"

    override val TAG: String
        get() = CategoryRepo::javaClass.toString()

    override fun loadRangeInternal(startPosition: Int, loadCount: Int, isInitial: Boolean, listener: Any) {

    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<DataModel.Category>) {

    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<DataModel.Category>) {

    }
}