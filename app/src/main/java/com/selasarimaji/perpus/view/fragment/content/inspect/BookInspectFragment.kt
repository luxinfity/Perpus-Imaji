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
import kotlinx.android.synthetic.main.content_book.*

class BookInspectFragment : BaseInspectFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_book, container, false)
    }

    override fun setupView(){
        arrayListOf<TextInputLayout>(bookNameInputLayout,
                bookAuthorInputLayout,
                yearInputLayout,
                publisherInputLayout,
                categoryListChipInput)
                .apply {
                    this.map {
                        it.editText?.inputType = InputType.TYPE_NULL
                    }
                }

        viewModel.getSelectedItemLiveData().observe(this, Observer {
            (it as DataModel.Book?)?.let {
                bookNameInputLayout.editText?.setText(it.name)
                bookAuthorInputLayout.editText?.setText(it.author)
                yearInputLayout.editText?.setText(it.year.toString())
                publisherInputLayout.editText?.setText(it.publisher)
                categoryListChipInput.editText?.setText(it.idCategoryList.toString())
            }
        })
    }

    override fun setupToolbar(){
        viewModel.getSelectedItemLiveData().observe(this, Observer {
            (it as DataModel.Book?)?.let {
                viewModel.title.value = it.name.toUpperCase()
            }
        })
    }
}
