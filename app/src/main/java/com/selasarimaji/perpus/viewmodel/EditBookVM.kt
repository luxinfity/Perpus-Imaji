package com.selasarimaji.perpus.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.repository.firebase.BaseRepo
import com.selasarimaji.perpus.repository.firebase.BookRepo

class EditBookVM : BaseContentVM<DataModel.Book>() {
    override val repo: BaseRepo<DataModel.Book>
        get() = BookRepo()

    val bookList  by lazy {
        val asd = MutableLiveData<PagedList<DataModel.Book>>().apply {

        }

        asd.value = PagedList.Builder(repo,
                PagedList.Config.Builder().setPageSize(10).setEnablePlaceholders(false).build()
        ).setInitialKey(0).build()
    }
}