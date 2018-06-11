package com.selasarimaji.perpus.viewmodel.content

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.JsonArray
import com.selasarimaji.perpus.model.Loading
import com.selasarimaji.perpus.model.RepoDataModel
import com.selasarimaji.perpus.model.RepoImage
import com.selasarimaji.perpus.repository.BookRepo
import com.selasarimaji.perpus.repository.CategoryRepo
import com.selasarimaji.perpus.viewmodel.BaseContentCreationVM
import com.selasarimaji.perpus.viewmodel.ImageContentVM

class BookVM : BaseContentCreationVM<RepoDataModel.Book>(), ImageContentVM<RepoDataModel.Book> {
    override val repo = BookRepo()

    // auto complete
    private var categoryQuery : String = ""
    val repoCategoryVal by lazy {
        CategoryRepo()
    }

    // image upload
    override val pickedImage = MutableLiveData<RepoImage>()

    fun getPossibleCategoryInputName(charSequence: CharSequence){
        if (charSequence.toString() != categoryQuery) { // blocking un needed response
            categoryQuery = charSequence.toString()
            if(categoryQuery.isNotEmpty())
                repoCategoryVal
                    .loadFromRemote(Loading.Param(filterMap = mapOf("name" to categoryQuery))){
                        if (it.data is QuerySnapshot) repoCategoryVal.onLoadCallback(it.data)
                        else if (it.data is JsonArray) repoCategoryVal.onLoadCallback(it.data)
                    }
        }
    }
}