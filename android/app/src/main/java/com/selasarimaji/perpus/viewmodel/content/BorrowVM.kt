package com.selasarimaji.perpus.viewmodel.content

import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.JsonArray
import com.selasarimaji.perpus.model.Loading
import com.selasarimaji.perpus.model.RepoDataModel
import com.selasarimaji.perpus.repository.BookRepo
import com.selasarimaji.perpus.repository.BorrowRepo
import com.selasarimaji.perpus.repository.KidRepo
import com.selasarimaji.perpus.viewmodel.BaseContentCreationVM

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
        isLoading.value.let {
            if (it != true){
                repo.loadFromRemote(Loading.Param(
                        Loading.Param.Position(0, loadDistance),
                        orderBy = "startDate",
                        filterMap = filterMap), isLoading){
                    if (it.data is QuerySnapshot) repo.onLoadCallback(it.data)
                    else if (it.data is JsonArray) repo.onLoadCallback(it.data)
                }
            }
        }
    }

    override fun loadMore(filterMap: Map<String, String>?) {
        isLoading.value.let {
            if (it != true){
                repo.loadFromRemote(Loading.Param(
                        Loading.Param.Position(repo.fetchedData.value!!.size, loadDistance),
                        orderBy = "startDate",
                        filterMap = filterMap), isLoading){
                    if (it.data is QuerySnapshot) repo.onLoadCallback(it.data)
                    else if (it.data is JsonArray) repo.onLoadCallback(it.data)
                }
            }
        }
    }

    fun getPossibleKidName(charSequence: CharSequence){
        if (charSequence.toString() != kidQuery) { // blocking un needed response
            kidQuery = charSequence.toString()
            if(kidQuery.isNotEmpty())
                repoKidVal.loadFromRemote(Loading.Param(filterMap = mapOf("name" to kidQuery))){
                    if (it.data is QuerySnapshot) repoKidVal.onLoadCallback(it.data)
                    else if (it.data is JsonArray) repoKidVal.onLoadCallback(it.data)
                }
        }
    }

    fun getPossibleBookName(charSequence: CharSequence){
        if (charSequence.toString() != bookQuery) { // blocking un needed response
            bookQuery = charSequence.toString()
            if(bookQuery.isNotEmpty())
                repoBookVal.loadFromRemote(Loading.Param(filterMap = mapOf("name" to bookQuery))){
                    if (it.data is QuerySnapshot) repoBookVal.onLoadCallback(it.data)
                    else if (it.data is JsonArray) repoBookVal.onLoadCallback(it.data)
                }
        }
    }
}