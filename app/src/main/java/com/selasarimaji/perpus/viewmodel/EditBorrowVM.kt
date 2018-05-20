package com.selasarimaji.perpus.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.QuerySnapshot
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.repository.firestore.BaseRepo
import com.selasarimaji.perpus.repository.firestore.BookRepo
import com.selasarimaji.perpus.repository.firestore.BorrowRepo
import com.selasarimaji.perpus.repository.firestore.KidRepo
import java.text.SimpleDateFormat
import java.util.*

class EditBorrowVM : BaseContentVM<DataModel.Borrow>() {

    override val TAG: String
        get() = EditBorrowVM::class.java.name

    private val repoVal by lazy {
        BorrowRepo()
    }
    override val repo: BaseRepo<DataModel.Borrow>
        get() = repoVal


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


    override fun loadInitial(){
        super.loadInitial()
        if (isInitialLoaded.value == null){
            lastIndex.value = 0
            isInitialLoaded.value = true
            repo.loadRange(0, 10, listener = this@EditBorrowVM::handleFirebaseQueryCallback)
        }
    }

    override fun loadMore() {
        isLoading.value?.run {
            if (!this){
                isLoading.value = true
                repo.loadRange(lastIndex.value!!, 10, listener = this@EditBorrowVM::handleFirebaseQueryCallback)
            }
        }
    }

    private fun handleFirebaseQueryCallback(querySnapshot: QuerySnapshot){
        querySnapshot.documents.map {
            repo.addLocalItem(DataModel.Borrow.turnDocumentToObject(it))
        }
        lastIndex.value = lastIndex.value!! + 10
        isLoading.value = false
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