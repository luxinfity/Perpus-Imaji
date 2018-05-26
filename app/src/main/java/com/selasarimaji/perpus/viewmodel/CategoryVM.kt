package com.selasarimaji.perpus.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.QuerySnapshot
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.repository.firestore.CategoryRepo

class CategoryVM : BaseContentCreationVM<DataModel.Category>() {
    override val repo = CategoryRepo()

    override val TAG: String
        get() = CategoryVM::class.java.name

    private var categoryQuery : String = ""
    val filteredCategory = MutableLiveData<List<DataModel.Category>>()

    override fun loadInitial(filterMap: Map<String, String>?){
        super.loadInitial(filterMap)
        if (isInitialLoaded.value == null){
            lastIndex.value = 0
            isInitialLoaded.value = true
            repo.load(0, loadCount, filterMap = filterMap,
                    listener = this@CategoryVM::handleFirebaseQueryCallback)
        }
    }

    override fun loadMore(filterMap: Map<String, String>?) {
        isContentLoading.value?.run {
            if (!this){
                isContentLoading.value = true
                repo.load(lastIndex.value!!, loadCount, filterMap = filterMap,
                        listener = this@CategoryVM::handleFirebaseQueryCallback)
            }
        }
    }

    private fun handleFirebaseQueryCallback(querySnapshot: QuerySnapshot){
        querySnapshot.documents.map {
            repo.addLocalItem(DataModel.Category.turnDocumentToObject(it))
        }
        lastIndex.value = lastIndex.value!! + loadCount
        isContentLoading.value = false
    }

    fun getPossibleCategoryInputName(charSequence: CharSequence){
        if (charSequence.toString() != categoryQuery) { // blocking un needed response
            categoryQuery = charSequence.toString()
            repo.getContentWith("name", categoryQuery) { querySnapshot, query ->
                if (query == categoryQuery) { // blocking un needed response
                    val list = querySnapshot.documents.map {
                        DataModel.Category.turnDocumentToObject(it)
                    }
                    filteredCategory.value = list
                }
            }
        }
    }
}