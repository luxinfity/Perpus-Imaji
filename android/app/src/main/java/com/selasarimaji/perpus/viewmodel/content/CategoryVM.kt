package com.selasarimaji.perpus.viewmodel.content

import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.JsonArray
import com.selasarimaji.perpus.model.Loading
import com.selasarimaji.perpus.model.RepoDataModel
import com.selasarimaji.perpus.repository.CategoryRepo
import com.selasarimaji.perpus.viewmodel.BaseContentCreationVM

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
                repoCategoryVal.loadFromRemote(Loading.Param(filterMap = mapOf("name" to categoryQuery))){
                    if (it.data is QuerySnapshot) repoCategoryVal.onLoadCallback(it.data)
                    else if (it.data is JsonArray) repoCategoryVal.onLoadCallback(it.data)
                }
        }
    }
}