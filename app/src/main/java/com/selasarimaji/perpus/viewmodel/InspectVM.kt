package com.selasarimaji.perpus.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.selasarimaji.perpus.model.RepoDataModel

class InspectVM : ViewModel(){
    private var selectedItem = MutableLiveData<RepoDataModel>()
    var editOrCreateMode = MutableLiveData<Pair<Boolean, Boolean>>()
    var title = MutableLiveData<String>()
    var shouldShowProgressBar = MutableLiveData<Boolean>()

    fun setSelectedItem(item: RepoDataModel?){
        item?.let {
            selectedItem.value = it
        }
//        this.repo = repo
    }

    fun getSelectedItemLiveData() = selectedItem
}