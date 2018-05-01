package com.selasarimaji.perpus.viewmodel

import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.repository.firebase.BaseRepo
import com.selasarimaji.perpus.repository.firebase.BorrowRepo

class EditBorrowVM : BaseContentVM<DataModel.Borrow>() {

    override val repo: BaseRepo<DataModel.Borrow>
        get() = BorrowRepo()
}