package com.selasarimaji.perpus.view.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.jakewharton.rxbinding2.widget.RxTextView
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.viewmodel.EditCategoryVM
import kotlinx.android.synthetic.main.activity_content_creation.*
import kotlinx.android.synthetic.main.content_category.*
import java.util.concurrent.TimeUnit

class CategoryCreationActivity : BaseContentCreationActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(EditCategoryVM::class.java)
    }

    private val parentCategoryText : String
        get() = categoryParentInputLayout.editText?.text.toString()

    override fun setupView(){
        val view = layoutInflater.inflate(R.layout.content_category, null)
        linearContainer.addView(view, 0)

        categoryPathInputLayout.visibility = View.GONE

        categoryParentInputLayout.editText?.let{
            RxTextView.textChanges(it)
                    .skip(1)
                    .debounce(300, TimeUnit.MILLISECONDS)
                    .subscribe {
                        viewModel.getPossibleCategoryInputName(it)
                    }
        }
    }

    override fun setupToolbar(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Kategori"
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
                (categoryParentInputLayout.editText as AutoCompleteTextView).run {
                    setAdapter(adapter)
                    showDropDown()
                }
            }
        })
    }

    override fun submitValue() {
        val editTextList = arrayListOf<TextInputLayout>(categoryNameInputLayout,
                categoryDescInputLayout).apply {
            this.map {
                it.error = null
                it.isErrorEnabled = false
            }
        }

        val name = categoryNameInputLayout.editText?.text.toString().toLowerCase().also {
            if (it.isNotEmpty()) {
                editTextList.remove(categoryNameInputLayout)
            }
        }
        val desc = categoryDescInputLayout.editText?.text.toString().toLowerCase().also {
            if (it.isNotEmpty()) {
                editTextList.remove(categoryDescInputLayout)
            }
        }
        val parent = categoryParentInputLayout.editText?.text.toString().toLowerCase()

        editTextList.map {
            it.error = "Silahkan diisi"
        }

        if(editTextList.isEmpty()) {
            viewModel.storeData(DataModel.Category(name, desc, parent))
        }
    }
}
