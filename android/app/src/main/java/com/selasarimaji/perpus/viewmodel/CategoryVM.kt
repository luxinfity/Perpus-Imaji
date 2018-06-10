package com.selasarimaji.perpus.viewmodel

import com.selasarimaji.perpus.model.RepoDataModel
import com.selasarimaji.perpus.repository.CategoryRepo

class CategoryVM : BaseContentCreationVM<RepoDataModel.Category>() {
    override val repo = CategoryRepo()

    private var categoryQuery : String = ""
    val repoCategoryVal by lazy {
        CategoryRepo()
    }

    fun getPossibleCategoryInputName(charSequence: CharSequence){
        if (charSequence.toString() != categoryQuery) { // blocking un needed response
            categoryQuery = charSequence.toString()
            if(categoryQuery.isNotEmpty())
                repoCategoryVal.loadFromRemote(filterMap = mapOf("name" to categoryQuery))
        }
    }

    fun getRealNameOfId(id: String, onResult: (String) -> Unit){
        if (id.isNotEmpty()) {
            repo.getRealNameOfId(id) {
                it?.let(onResult)
            }
        }
    }
}