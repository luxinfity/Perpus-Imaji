package com.selasarimaji.perpus.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.selasarimaji.perpus.model.RepoDataModel
import com.selasarimaji.perpus.model.RepoImage
import com.selasarimaji.perpus.repository.KidRepo

class KidVM : BaseContentCreationVM<RepoDataModel.Kid>() {
    override val repo = KidRepo()

    val pickedImage = MutableLiveData<RepoImage>()

    fun imagePickActivityResult(image: RepoImage){
        pickedImage.value = image
    }

    fun storeImage() {
        repo.storeImage(pickedImage.value!!.imagePath, documentResultRef.value!!.id, loadingProcess)
    }
}
