package com.selasarimaji.perpus.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.esafirm.imagepicker.model.Image
import com.google.firebase.firestore.QuerySnapshot
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.repository.firestore.KidRepo

class KidVM : BaseContentCreationVM<DataModel.Kid>() {
    override val repo = KidRepo()

    override val TAG: String
        get() = KidVM::class.java.name

    val pickedImage = MutableLiveData<Image>()
    val uploadingProgress = MutableLiveData<Double>()

    override fun loadInitial(filterMap: Map<String, String>?){
        super.loadInitial(filterMap)
        if (isInitialLoaded.value == null){
            lastIndex.value = 0
            isInitialLoaded.value = true
            repo.load(0, loadCount, filterMap = filterMap,
                    listener = this@KidVM::handleFirebaseQueryCallback)
        }
    }

    override fun loadMore(filterMap: Map<String, String>?) {
        isContentLoading.value?.run {
            if (!this){
                isContentLoading.value = true
                repo.load(lastIndex.value!!, loadCount, filterMap = filterMap,
                        listener = this@KidVM::handleFirebaseQueryCallback)
            }
        }
    }

    private fun handleFirebaseQueryCallback(querySnapshot: QuerySnapshot){
        querySnapshot.documents.map {
            repo.addLocalItem(DataModel.Kid.turnDocumentToObject(it))
        }
        lastIndex.value = lastIndex.value!! + loadCount
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
