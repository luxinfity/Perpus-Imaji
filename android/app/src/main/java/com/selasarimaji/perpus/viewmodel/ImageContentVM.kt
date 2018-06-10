package com.selasarimaji.perpus.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.selasarimaji.perpus.model.*
import com.selasarimaji.perpus.repository.BaseRepo

interface ImageContentVM<T: RepoDataModel> {

    val pickedImage : MutableLiveData<RepoImage>

    fun imagePickActivityResult(image: RepoImage){
        pickedImage.value = image
    }
    fun storeImage(repo: BaseRepo<T>,
                   documentId: String,
                   isLoading: MutableLiveData<Boolean>,
                   onResult: (Loading.Result<Boolean>) -> Unit) {
        repo.storeImage(pickedImage.value!!.imagePath, documentId, isLoading, onResult)
    }

    fun deleteImage(repo: BaseRepo<T>,
                    documentId: String,
                    isLoading: MutableLiveData<Boolean>,
                    onResult: (Loading.Result<Boolean>) -> Unit){
        repo.deleteImage(documentId, isLoading, onResult)
    }

    // status check helper
    val userImagePathExist
        get() = !pickedImage.value?.imagePath.isNullOrEmpty()
    val userHasRemoteImage
        get() = pickedImage.value?.isRemoteSource == true && userImagePathExist
    val userHasLocalImage
        get() = pickedImage.value?.isRemoteSource == false && userImagePathExist

}