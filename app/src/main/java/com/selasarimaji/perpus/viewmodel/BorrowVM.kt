package com.selasarimaji.perpus.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.QuerySnapshot
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.repository.firestore.BookRepo
import com.selasarimaji.perpus.repository.firestore.BorrowRepo
import com.selasarimaji.perpus.repository.firestore.KidRepo

class BorrowVM : BaseContentCreationVM<DataModel.Borrow>() {
    override val TAG: String
        get() = BorrowVM::class.java.name

    override val repo = BorrowRepo()

    // auto complete
    private val repoKidVal by lazy {
        KidRepo()
    }
    private var kidQuery : String = ""
    val filteredKid = MutableLiveData<List<DataModel.Kid>>()

    private val repoBookVal by lazy {
        BookRepo()
    }
    private var bookQuery : String = ""
    val filteredBook = MutableLiveData<List<DataModel.Book>>()


    override fun loadInitial(filterMap: Map<String, String>?){
        super.loadInitial(filterMap)
        if (isInitialLoaded.value == null){
            lastIndex.value = 0
            isInitialLoaded.value = true
            repo.load(0, loadCount, "startDate", filterMap = filterMap,
                    listener = this@BorrowVM::handleFirebaseQueryCallback)
        }
    }

    override fun loadMore(filterMap: Map<String, String>?) {
        isContentLoading.value?.run {
            if (!this){
                isContentLoading.value = true
                repo.load(lastIndex.value!!, loadCount, "startDate", filterMap = filterMap,
                        listener = this@BorrowVM::handleFirebaseQueryCallback)
            }
        }
    }

    private fun handleFirebaseQueryCallback(querySnapshot: QuerySnapshot){
        querySnapshot.documents.map {
            repo.addLocalItem(DataModel.Borrow.turnDocumentToObject(it))
        }
        lastIndex.value = lastIndex.value!! + loadCount
        isContentLoading.value = false
    }

    fun getPossibleKidName(charSequence: CharSequence){
        if (charSequence.toString() != kidQuery) { // blocking un needed response
            kidQuery = charSequence.toString()
            repoKidVal.getContentWith("name", kidQuery) { querySnapshot, query ->
                if (query == kidQuery) { // blocking un needed response
                    val list = querySnapshot.documents.map {
                        DataModel.Kid.turnDocumentToObject(it)
                    }
                    filteredKid.value = list
                }
            }
        }
    }

    fun getPossibleBookName(charSequence: CharSequence){
        if (charSequence.toString() != bookQuery) { // blocking un needed response
            bookQuery = charSequence.toString()
            repoBookVal.getContentWith("name", bookQuery) { querySnapshot, query ->
                if (query == bookQuery) { // blocking un needed response
                    val list = querySnapshot.documents.map {
                        DataModel.Book.turnDocumentToObject(it)
                    }
                    filteredBook.value = list
                }
            }
        }
    }
}