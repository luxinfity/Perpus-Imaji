package com.selasarimaji.perpus.view.fragment.content.inspect

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.model.DataModel
import kotlinx.android.synthetic.main.content_category.*

class CategoryInspectFragment : BaseInspectFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_category, container, false)
    }

    override fun setupView(){
        arrayListOf<TextInputLayout>(categoryNameInputLayout,
                categoryDescInputLayout,
                categoryParentInputLayout,
                categoryPathInputLayout)
                .apply {
                    this.map {
                        it.editText?.inputType = InputType.TYPE_NULL
                    }
                }

        viewModel.getSelectedItemLiveData().observe(this, Observer {
            (it as DataModel.Category?)?.let {
                categoryNameInputLayout.editText?.setText(it.name)
                categoryDescInputLayout.editText?.setText(it.description)
                categoryParentInputLayout.editText?.setText(it.idParent)
                categoryPathInputLayout.editText?.setText(it.idParent)
            }
        })
    }

    override fun setupToolbar(){
        viewModel.getSelectedItemLiveData().observe(this, Observer {
            (it as DataModel.Category?)?.let {
                viewModel.title.value = it.name.toUpperCase()
            }
        })
    }
}
