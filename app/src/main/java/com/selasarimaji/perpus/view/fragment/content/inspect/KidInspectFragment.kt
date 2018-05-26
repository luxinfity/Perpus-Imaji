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
import kotlinx.android.synthetic.main.content_kid.*

class KidInspectFragment : BaseInspectFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_kid, container, false)
    }

    override fun setupView(){
        arrayListOf<TextInputLayout>(kidNameInputLayout,
                kidAddressInputLayout,
                kidBirthDateInputLayout)
                .apply {
                    this.map {
                        it.editText?.inputType = InputType.TYPE_NULL
                    }
                }

        viewModel.getSelectedItemLiveData().observe(this, Observer {
            (it as DataModel.Kid?)?.let {
                kidNameInputLayout.editText?.setText(it.name)
                kidAddressInputLayout.editText?.setText(it.address)
                kidBirthDateInputLayout.editText?.setText(it.birthDate)
            }
        })
    }

    override fun setupToolbar(){
        viewModel.getSelectedItemLiveData().observe(this, Observer {
            (it as DataModel.Kid?)?.let {
                viewModel.title.value = it.name.toUpperCase()
            }
        })
    }
}
