package com.selasarimaji.perpus.view.activity

import android.os.Bundle
import com.selasarimaji.perpus.R
import kotlinx.android.synthetic.main.activity_content_creation.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_creation)

        setupView()
        setupToolbar()
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
}
