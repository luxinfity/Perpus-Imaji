package com.selasarimaji.perpus.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.selasarimaji.perpus.model.RepoDataModel
import com.selasarimaji.perpus.model.RepoImage
import com.selasarimaji.perpus.repository.BookRepo
import com.selasarimaji.perpus.repository.CategoryRepo

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
                repoCategoryVal.loadFromRemote(filterMap = mapOf("name" to categoryQuery))
        }
    }
}