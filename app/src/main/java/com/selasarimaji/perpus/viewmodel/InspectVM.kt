package com.selasarimaji.perpus.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.selasarimaji.perpus.model.RepoDataModel

class InspectVM : ViewModel(){
    private var selectedItem = MutableLiveData<RepoDataModel>()
    var editOrCreateMode = MutableLiveData<Pair<Boolean, Boolean>>()

    fun setSelectedItem(item: RepoDataModel?){
        item?.let {
            selectedItem.value = it
        }
    }

    fun getSelectedItemLiveData() = selectedItem
}