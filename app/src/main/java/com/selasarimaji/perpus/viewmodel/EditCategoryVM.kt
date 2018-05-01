package com.selasarimaji.perpus.viewmodel

import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.repository.firebase.BaseRepo
import com.selasarimaji.perpus.repository.firebase.CategoryRepo

class EditCategoryVM : BaseContentVM<DataModel.Category>() {
    override val repo: BaseRepo<DataModel.Category>
        get() = CategoryRepo()
}