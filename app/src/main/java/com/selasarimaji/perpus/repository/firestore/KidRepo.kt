package com.selasarimaji.perpus.repository.firestore

import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.selasarimaji.perpus.model.DataModel
import java.io.File

class KidRepo : BaseRepo<DataModel.Kid>() {
    companion object {
        const val IMAGE_PATH = "content/kid/"
    }
    override val collectionName: String
        get() = "content/kid/list"

    private val liveData by lazy {
        MutableLiveData<List<DataModel.Kid>>()
    }
    private val imageFolderRef by lazy {
        FirebaseStorage.getInstance().getReference(IMAGE_PATH)
    }

    override val fetchedData: MutableLiveData<List<DataModel.Kid>>
        get() = liveData

    fun storeImage(filePath: String, docId: String, loadingFlag: MutableLiveData<Boolean>,
                   successFlag: MutableLiveData<Boolean>, uploadProgress : MutableLiveData<Double>){
        loadingFlag.value = true

        val file = Uri.fromFile(File(filePath))
        val remoteFile = imageFolderRef.child("$docId.jpg")
        remoteFile.putFile(file)
                .addOnProgressListener {
                    uploadProgress.value = 100.0 * it.bytesTransferred / it.totalByteCount
                }
                .addOnCompleteListener {
                    loadingFlag.value = false
                    successFlag.value = it.isSuccessful
                }
    }
}