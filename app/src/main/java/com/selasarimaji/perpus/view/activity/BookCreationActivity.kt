package com.selasarimaji.perpus.view.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.hootsuite.nachos.NachoTextView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.viewmodel.EditBookVM
import kotlinx.android.synthetic.main.activity_content_creation.*
import kotlinx.android.synthetic.main.content_book.*
import java.util.concurrent.TimeUnit

class BookCreationActivity : BaseContentCreationActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(EditBookVM::class.java)
    }

    private val parentCategoryText : String
        get() = (categoryListChipInput.editText as  NachoTextView).tokenValues.let {
            if (it.size > 0) it.last()
            else ""
        }

    override fun setupView(){
        val view = layoutInflater.inflate(R.layout.content_book, null)
        linearContainer.addView(view, 0)

        categoryListChipInput.editText?.let{
            RxTextView.textChanges(it)
                    .skip(1)
                    .debounce(300, TimeUnit.MILLISECONDS)
                    .subscribe {
                        viewModel.getPossibleCategoryInputName(parentCategoryText)
                    }
        }
    }

    override fun setupToolbar(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Buku"
    }

    override fun setupObserver(){
        viewModel.uploadingFlag.observe(this, Observer<Boolean> {
            it?.run {
                progressBar.visibility = if (this) View.VISIBLE else View.GONE
                addButton.isEnabled = !this
            }
        })
        viewModel.uploadingSuccessFlag.observe(this, Observer<Boolean> {
            it?.run {
                if(this) {
                    Toast.makeText(applicationContext,
                            "Penambahan Berhasil",
                            Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        })
        viewModel.filteredCategory.observe(this, Observer<List<DataModel.Category>>{
            it?.run {
                val adapter = ArrayAdapter<String>(applicationContext,
                        android.R.layout.simple_dropdown_item_1line,
                        this.filter { it.name.contains(parentCategoryText) }.map { it.name.capitalize() })
                (categoryListChipInput.editText as NachoTextView).run {
                    setAdapter(adapter)
                    showDropDown()
                }
            }
        })
    }

    override fun submitValue() {
        val editTextList = arrayListOf<TextInputLayout>(bookNameInputLayout,
                bookAuthorInputLayout, yearInputLayout,
                publisherInputLayout, categoryListChipInput).apply {
            this.map {
                it.error = null
                it.isErrorEnabled = false
            }
        }

        val name = bookNameInputLayout.editText?.text.toString().toLowerCase().also {
            if (it.isNotEmpty()) {
                editTextList.remove(bookNameInputLayout)
            }
        }
        val author = bookAuthorInputLayout.editText?.text.toString().toLowerCase().also {
            if (it.isNotEmpty()) {
                editTextList.remove(bookAuthorInputLayout)
            }
        }
        val year = yearInputLayout.editText?.text.toString().also {
            if (it.isNotEmpty()) {
                editTextList.remove(yearInputLayout)
            }
        }
        val publisher = publisherInputLayout.editText?.text.toString().toLowerCase().also {
            if (it.isNotEmpty()) {
                editTextList.remove(publisherInputLayout)
            }
        }
        val category = (categoryListChipInput.editText as NachoTextView).chipValues.map {
            val value = it
            viewModel.filteredCategory.value?.
                    find { it.name == value.toLowerCase() }?.id
                    ?: ""
        }.also {
            if (it.isNotEmpty()) {
                editTextList.remove(categoryListChipInput)
            }
        }

        editTextList.map {
            if (it.error.isNullOrEmpty()) it.error = "Silahkan diisi"
        }

        if(editTextList.isEmpty()){
            viewModel.storeData(DataModel.Book(name, author, year.toInt(), publisher, category))
        }
    }
}
