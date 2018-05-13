package com.selasarimaji.perpus.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.view.View
import android.widget.Toast
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.viewmodel.EditCategoryVM
import kotlinx.android.synthetic.main.activity_content_creation.*
import kotlinx.android.synthetic.main.content_category.*


class CategoryCreationActivity : BaseContentCreationActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(EditCategoryVM::class.java)
    }

    override fun setupView(){
        val view = layoutInflater.inflate(R.layout.content_category, null)
        linearContainer.addView(view, 0)
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
                    finish()
                }
            }
        })
    }

    override fun submitValue() {
        val name = categoryNameInputLayout.editText?.text.toString()
        val desc = categoryDescInputLayout.editText?.text.toString()
        val parent = categoryParentInputLayout.editText?.text.toString()

        viewModel.storeData(DataModel.Category(name, desc, parent))
    }
}
