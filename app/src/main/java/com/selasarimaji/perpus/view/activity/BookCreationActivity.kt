package com.selasarimaji.perpus.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.view.View
import android.widget.Toast
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.viewmodel.EditBookVM
import kotlinx.android.synthetic.main.activity_content_creation.*
import kotlinx.android.synthetic.main.content_book.*

class BookCreationActivity : BaseContentCreationActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(EditBookVM::class.java)
    }

    override fun setupView(){
        val view = layoutInflater.inflate(R.layout.content_book, null)
        linearContainer.addView(view, 0)
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
                    finish()
                }
            }
        })
    }

    override fun submitValue() {
        val name = bookNameInputLayout.editText?.text.toString()
        val author = bookAuthorInputLayout.editText?.text.toString()
        val year = yearInputLayout.editText?.text.toString().toInt()
        val publisher = publisherInputLayout.editText?.text.toString()
        val category = categoryListChipInput.text.toString()

        viewModel.storeData(DataModel.Book(name, author, year, publisher, category))
    }
}
