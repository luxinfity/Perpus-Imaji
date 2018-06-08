package com.selasarimaji.perpus.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.selasarimaji.perpus.model.LoadingProcess
import com.selasarimaji.perpus.model.LoadingType
import com.selasarimaji.perpus.model.RepoDataModel
import com.selasarimaji.perpus.model.RepoImage
import com.selasarimaji.perpus.repository.BaseRepo

interface ImageContentVM<T: RepoDataModel> {

    val pickedImage : MutableLiveData<RepoImage>

    fun imagePickActivityResult(image: RepoImage){
        pickedImage.value = image
    }
    fun storeImage(repo: BaseRepo<T>,
                   documentId: String,
                   loadingProcess: MutableLiveData<LoadingProcess>) {
        repo.storeImage(pickedImage.value!!.imagePath, documentId, loadingProcess){
            if (it){
                pickedImage.value = RepoImage(pickedImage.value!!.imagePath, true)
            }
        }
    }

    fun deleteImage(repo: BaseRepo<T>,
                    documentId: String,
                    loadingProcess: MutableLiveData<LoadingProcess>){
        repo.deleteImage(documentId, loadingProcess){
            if(it){
                pickedImage.value = RepoImage("", false)
            }
        }
    }

    // status check helper
    val userImagePathExist
        get() = !pickedImage.value?.imagePath.isNullOrEmpty()
    val userHasLocalImage
        get() = pickedImage.value?.isRemoteSource == false && userImagePathExist
    val userHasRemoteImage
        get() = pickedImage.value?.isRemoteSource ?: false && userImagePathExist

    fun isUserWantToDeleteRemoteImage(loadingType: LoadingType)
            = userHasRemoteImage && loadingType == LoadingType.Delete
    fun isUserWantToUpdateRemoteImage(loadingType: LoadingType)
            = userHasRemoteImage && loadingType == LoadingType.Update
}