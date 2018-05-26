package com.selasarimaji.perpus.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.selasarimaji.perpus.model.DataModel

class InspectVM : ViewModel(){
    private var selectedItem = MutableLiveData<DataModel>()
    var editOrCreateMode = MutableLiveData<Pair<Boolean, Boolean>>()
    var title = MutableLiveData<String>()

    fun setSelectedItem(item: DataModel?){
        item?.let {
            selectedItem.value = it
        }
//        this.repo = repo
    }

    fun getSelectedItemLiveData() = selectedItem
}