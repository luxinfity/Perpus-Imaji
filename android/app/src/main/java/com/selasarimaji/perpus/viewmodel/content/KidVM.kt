package com.selasarimaji.perpus.viewmodel.content

import android.arch.lifecycle.MutableLiveData
import com.selasarimaji.perpus.model.RepoDataModel
import com.selasarimaji.perpus.model.RepoImage
import com.selasarimaji.perpus.repository.KidRepo
import com.selasarimaji.perpus.viewmodel.BaseContentCreationVM
import com.selasarimaji.perpus.viewmodel.ImageContentVM

class KidVM : BaseContentCreationVM<RepoDataModel.Kid>(), ImageContentVM<RepoDataModel.Kid> {
    override val repo = KidRepo()
    override val pickedImage = MutableLiveData<RepoImage>()
}
