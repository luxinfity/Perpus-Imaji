package com.selasarimaji.perpus.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.esafirm.imagepicker.model.Image
import com.google.firebase.firestore.QuerySnapshot
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.repository.firestore.BaseRepo
import com.selasarimaji.perpus.repository.firestore.KidRepo

class EditKidVM : BaseContentVM<DataModel.Kid>() {
    override val repo = KidRepo()

    override val TAG: String
        get() = EditKidVM::class.java.name

    val pickedImage = MutableLiveData<Image>()
    val uploadingProgress = MutableLiveData<Double>()

    override fun loadInitial(){
        super.loadInitial()
        if (isInitialLoaded.value == null){
            lastIndex.value = 0
            isInitialLoaded.value = true
            repo.loadRange(0, 10, listener = this@EditKidVM::handleFirebaseQueryCallback)
        }
    }

    override fun loadMore() {
        isContentLoading.value?.run {
            if (!this){
                isContentLoading.value = true
                repo.loadRange(lastIndex.value!!, 10, listener = this@EditKidVM::handleFirebaseQueryCallback)
            }
        }
    }

    private fun handleFirebaseQueryCallback(querySnapshot: QuerySnapshot){
        querySnapshot.documents.map {
            repo.addLocalItem(DataModel.Kid.turnDocumentToObject(it))
        }
        lastIndex.value = lastIndex.value!! + 10
        isContentLoading.value = false
    }

    fun imagePickActivityResult(image: Image){
        pickedImage.value = image
    }

    fun storeImage(){
        repo.storeImage(pickedImage.value!!.path, documentResultRef.value!!.id,
                uploadingFlag, uploadingSuccessFlag, uploadingProgress)
    }

    fun shouldWaitImageUpload() : Boolean = pickedImage.value != null && uploadingProgress.value == null
}
