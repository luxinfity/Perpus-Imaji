package com.selasarimaji.perpus.viewmodel.activity

import com.selasarimaji.perpus.model.Model
import com.selasarimaji.perpus.repository.firebase.CategoryRepo


class EditCategoryVM : BaseLoadingVM() {

    private val categoryRepo = CategoryRepo()

    fun storeData(category: Model.Category){
        categoryRepo.storeNewData(category, uploadingFlag, uploadingSuccessFlag)
    }
}