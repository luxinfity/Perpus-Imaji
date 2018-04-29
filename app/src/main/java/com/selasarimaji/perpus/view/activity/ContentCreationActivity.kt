package com.selasarimaji.perpus.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.model.Model
import com.selasarimaji.perpus.viewmodel.activity.EditCategoryVM
import com.selasarimaji.perpus.viewmodel.activity.TempVM
import kotlinx.android.synthetic.main.activity_content_creation.*
import kotlinx.android.synthetic.main.content_category.*

class ContentCreationActivity : BaseNavigationActivity() {
    companion object {
        const val CONTENT_TYPE_KEY = "CONTENT_TYPE_KEY"
    }

    enum class ContentType {
        Book, Category, Kid, Borrow
    }

    private val contentType by lazy {
        intent.extras?.getSerializable(CONTENT_TYPE_KEY) ?: ContentType.Book
    }

    private val viewModel by lazy {
        when(contentType){
            ContentType.Category -> ViewModelProviders.of(this).get(EditCategoryVM::class.java)
            else -> ViewModelProviders.of(this).get(TempVM::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_creation)

        setupView()
        setupToolbar()
        setupObserver()
        addButton.setOnClickListener {
            when(contentType){
                ContentType.Category -> addCategory()
            }
        }
    }

    private fun setupView(){
        val layoutId = when(contentType){
            ContentType.Category -> R.layout.content_category
            ContentType.Book -> R.layout.content_book
            ContentType.Borrow -> R.layout.content_borrow
            ContentType.Kid -> R.layout.content_kid
            else -> R.layout.content_book
        }
        val view = layoutInflater.inflate(layoutId, null)
        linearContainer.addView(view, 0)
    }

    private fun setupToolbar(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "${when(contentType){
            ContentType.Category -> "Kategori"
            ContentType.Book -> "Buku"
            ContentType.Borrow -> "Peminjaman"
            ContentType.Kid -> "Anak"
            else -> "Buku"
        }} Baru"
    }

    private fun setupObserver(){
        viewModel.initLiveData()
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

    private fun addCategory() {
        val name = categoryNameInputLayout.editText?.text.toString()
        val desc = categoryDescInputLayout.editText?.text.toString()
        val parent = categoryParentInputLayout.editText?.text.toString()

        (viewModel as EditCategoryVM).storeData(Model.Category(name, desc, parent))
    }
}
