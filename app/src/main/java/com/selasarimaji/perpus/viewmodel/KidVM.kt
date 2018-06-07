package com.selasarimaji.perpus.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.selasarimaji.perpus.model.RepoDataModel
import com.selasarimaji.perpus.model.RepoImage
import com.selasarimaji.perpus.repository.KidRepo

class KidVM : BaseContentCreationVM<RepoDataModel.Kid>(), ImageContentVM<RepoDataModel.Kid> {
    override val repo = KidRepo()
    override val pickedImage = MutableLiveData<RepoImage>()
}
