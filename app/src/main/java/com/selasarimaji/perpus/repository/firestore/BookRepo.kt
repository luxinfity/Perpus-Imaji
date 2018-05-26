package com.selasarimaji.perpus.repository.firestore

import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.selasarimaji.perpus.model.DataModel
import java.io.File

class BookRepo : BaseRepo<DataModel.Book>() {
    companion object {
        const val IMAGE_PATH = "content/book/"
    }

    private val imageFolderRef by lazy {
        FirebaseStorage.getInstance().getReference(IMAGE_PATH)
    }

    override val collectionName: String
        get() = "content/book/list"

    private val liveData by lazy {
        MutableLiveData<List<DataModel.Book>>()
    }

    override val fetchedData: MutableLiveData<List<DataModel.Book>>
        get() = liveData

    fun storeImage(filePath: String, docId: String, loadingFlag: MutableLiveData<Boolean>,
                   successFlag: MutableLiveData<Boolean>, uploadProgress: MutableLiveData<Double>){
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

    fun getImageFull(docId: String) = imageFolderRef.child("$docId.jpg")
    fun getImageThumb(docId: String) = imageFolderRef.child("thumb_$docId.jpg")
}