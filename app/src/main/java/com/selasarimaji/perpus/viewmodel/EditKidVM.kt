package com.selasarimaji.perpus.viewmodel

import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.repository.firebase.BaseRepo
import com.selasarimaji.perpus.repository.firebase.KidRepo

class EditKidVM : BaseContentVM<DataModel.Kid>() {
    override val repo: BaseRepo<DataModel.Kid>
        get() = KidRepo()
}
