package com.selasarimaji.perpus.viewmodel

import com.selasarimaji.perpus.model.RepoDataModel
import com.selasarimaji.perpus.repository.BookRepo
import com.selasarimaji.perpus.repository.BorrowRepo
import com.selasarimaji.perpus.repository.KidRepo

class BorrowVM : BaseContentCreationVM<RepoDataModel.Borrow>() {
    override val repo = BorrowRepo()

    // auto complete
    private var kidQuery : String = ""
    val repoKidVal by lazy {
        KidRepo()
    }

    private var bookQuery : String = ""
    val repoBookVal by lazy {
        BookRepo()
    }

    override fun loadInitial(filterMap: Map<String, String>?){
        getTotalRemoteCount()
        loadingProcess.value.let {
            if (it?.isLoading != true){
                repo.loadFromRemote(0,
                        loadDistance,
                        orderBy = "startDate",
                        filterMap = filterMap,
                        loadingFlag = loadingProcess)
            }
        }
    }

    override fun loadMore(filterMap: Map<String, String>?) {
        loadingProcess.value?.let {
            if (!it.isLoading){
                repo.loadFromRemote(repo.fetchedData.value!!.size,
                        loadDistance,
                        orderBy = "startDate",
                        filterMap = filterMap,
                        loadingFlag = loadingProcess)
            }
        }
    }

    fun getPossibleKidName(charSequence: CharSequence){
        if (charSequence.toString() != kidQuery) { // blocking un needed response
            kidQuery = charSequence.toString()
            if(kidQuery.isNotEmpty())
                repoKidVal.loadFromRemote(filterMap = mapOf("name" to kidQuery))
        }
    }

    fun getPossibleBookName(charSequence: CharSequence){
        if (charSequence.toString() != bookQuery) { // blocking un needed response
            bookQuery = charSequence.toString()
            if(bookQuery.isNotEmpty())
                repoBookVal.loadFromRemote(filterMap = mapOf("name" to bookQuery))
        }
    }
}