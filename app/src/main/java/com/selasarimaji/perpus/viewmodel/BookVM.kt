package com.selasarimaji.perpus.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.esafirm.imagepicker.model.Image
import com.google.firebase.firestore.QuerySnapshot
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.model.MyImage
import com.selasarimaji.perpus.repository.firestore.BookRepo
import com.selasarimaji.perpus.repository.firestore.CategoryRepo

class BookVM : BaseContentCreationVM<DataModel.Book>() {
    override val TAG: String
        get() = BookVM::class.java.name

    override val repo = BookRepo()

    // Auto complete
    private val repoCategoryVal by lazy {
        CategoryRepo()
    }
    private var categoryQuery : String = ""
    val filteredCategory = MutableLiveData<List<DataModel.Category>>()

    // image upload
    val pickedImage = MutableLiveData<MyImage>()
    val uploadingProgress = MutableLiveData<Double>()

    override fun loadInitial(filterMap: Map<String, String>?){
        super.loadInitial(filterMap)
        if (isInitialLoaded.value == null){
            lastIndex.value = 0
            isInitialLoaded.value = true
            repo.loadFromRemote(0, loadCount, filterMap = filterMap, listener = this@BookVM::handleFirebaseQueryCallback)
        }
    }

    override fun loadMore(filterMap: Map<String, String>?) {
        isContentLoading.value?.run {
            if (!this){
                isContentLoading.value = true
                repo.loadFromRemote(lastIndex.value!!, loadCount, filterMap = filterMap, listener = this@BookVM::handleFirebaseQueryCallback)
            }
        }
    }

    private fun handleFirebaseQueryCallback(querySnapshot: QuerySnapshot){
        querySnapshot.documents.map {
            repo.addLocalItem(DataModel.Book.turnDocumentToObject(it))
        }
        lastIndex.value = lastIndex.value!! + loadCount
        isContentLoading.value = false
    }

    fun getPossibleCategoryInputName(charSequence: CharSequence){
        if (charSequence.toString() != categoryQuery) { // blocking un needed response
            categoryQuery = charSequence.toString()
            repoCategoryVal.getContentWith("name", categoryQuery) { querySnapshot, query ->
                if (query == categoryQuery) { // blocking un needed response
                    val list = querySnapshot.documents.map {
                        DataModel.Category.turnDocumentToObject(it)
                    }
                    filteredCategory.value = list
                }
            }
        }
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