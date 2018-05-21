package com.selasarimaji.perpus.view.fragment.content

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
import com.selasarimaji.perpus.capitalizeWords
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.viewmodel.EditCategoryVM
import kotlinx.android.synthetic.main.activity_base_content_creation.*
import kotlinx.android.synthetic.main.content_category.*
import java.util.concurrent.TimeUnit

class CategoryCreationFragment : BaseCreationFragment() {
    override val viewModel by lazy {
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
        viewModel.title.value = "Kategori"
    }

    override fun setupObserver(){
        viewModel.uploadingFlag.observe(this, Observer {
            it?.run {
                addButton.isEnabled = !this
            }
        })
        viewModel.uploadingSuccessFlag.observe(this, Observer {
            it?.run {
                if(this) {
                    Toast.makeText(context,
                            "Penambahan Berhasil",
                            Toast.LENGTH_SHORT).show()
                    activity?.let {
                        it.setResult(Activity.RESULT_OK)
                        it.finish()
                    }
                }
            }
        })
        viewModel.filteredCategory.observe(this, Observer {
            it?.run {
                val adapter = ArrayAdapter<String>(context,
                        android.R.layout.simple_dropdown_item_1line,
                        this.filter { it.name.contains(parentCategoryText) }.map { it.name.capitalizeWords() })
                (categoryParentInputLayout.editText as AutoCompleteTextView).run {
                    setAdapter(adapter)
                    showDropDown()
                }
            }
        })
    }

    override fun submitValue() {
        val editTextList = arrayListOf<TextInputLayout>(categoryNameInputLayout,
                categoryDescInputLayout, categoryParentInputLayout).apply {
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

        val parent = viewModel.filteredCategory.value?.find {
            it.name == parentCategoryText.toLowerCase()
        }?.id.also {
            if (!it.isNullOrEmpty()) {
                editTextList.remove(categoryParentInputLayout)
            }
        }

        editTextList.map {
            if (it.error.isNullOrEmpty()) it.error = "Silahkan diisi"
        }

        if(editTextList.isEmpty()) {
            viewModel.storeData(DataModel.Category(name, desc, parent))
        }
    }
}
