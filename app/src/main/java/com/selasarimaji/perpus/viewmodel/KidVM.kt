package com.selasarimaji.perpus.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.esafirm.imagepicker.model.Image
import com.google.firebase.firestore.QuerySnapshot
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.model.MyImage
import com.selasarimaji.perpus.repository.firestore.KidRepo

class KidVM : BaseContentCreationVM<DataModel.Kid>() {
    override val repo = KidRepo()

    override val TAG: String
        get() = KidVM::class.java.name

    val pickedImage = MutableLiveData<MyImage>()
    val uploadingProgress = MutableLiveData<Double>()

    override fun loadInitial(filterMap: Map<String, String>?){
        super.loadInitial(filterMap)
        if (isInitialLoaded.value == null){
            lastIndex.value = 0
            isInitialLoaded.value = true
            repo.loadFromRemote(0, loadCount, filterMap = filterMap,
                    listener = this@KidVM::handleFirebaseQueryCallback)
        }
    }

    override fun loadMore(filterMap: Map<String, String>?) {
        isContentLoading.value?.run {
            if (!this){
                isContentLoading.value = true
                repo.loadFromRemote(lastIndex.value!!, loadCount, filterMap = filterMap,
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

    fun imagePickActivityResult(image: MyImage){
        pickedImage.value = image
    }

    fun storeImage(){
        repo.storeImage(pickedImage.value!!.localImage!!.path, documentResultRef.value!!.id,
                uploadingFlag, uploadingSuccessFlag, uploadingProgress)
    }

    fun shouldWaitImageUpload() : Boolean = pickedImage.value != null && uploadingProgress.value == null
}
