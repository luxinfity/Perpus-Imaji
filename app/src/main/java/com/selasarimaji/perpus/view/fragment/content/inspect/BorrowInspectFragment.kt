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
import kotlinx.android.synthetic.main.content_borrow.*

class BorrowInspectFragment : BaseInspectFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_borrow, container, false)
    }

    override fun setupView(){
        arrayListOf<TextInputLayout>(borrowBookInputLayout,
                borrowNameInputLayout,
                borrowStartDateInputLayout,
                borrowEndDateInputLayout)
                .apply {
                    this.map {
                        it.editText?.inputType = InputType.TYPE_NULL
                    }
                }

        viewModel.getSelectedItemLiveData().observe(this, Observer {
            (it as DataModel.Borrow?)?.let {
                borrowBookInputLayout.editText?.setText(it.idBook)
                borrowNameInputLayout.editText?.setText(it.idChild)
                borrowStartDateInputLayout.editText?.setText(it.startDate)
                borrowEndDateInputLayout.editText?.setText(it.endDate)
            }
        })
    }

    override fun setupToolbar(){
        viewModel.getSelectedItemLiveData().observe(this, Observer {
            (it as DataModel.Borrow?)?.let {
                viewModel.title.value = it.id.toUpperCase()
            }
        })
    }
}
