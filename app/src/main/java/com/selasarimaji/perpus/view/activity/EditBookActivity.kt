package com.selasarimaji.perpus.view.activity

import android.os.Bundle
import com.selasarimaji.perpus.R
import kotlinx.android.synthetic.main.activity_edit_book.*


class EditBookActivity : BaseContentCreationActivity() {
    override val newItemTitle: String
        get() = "Buku Baru"

    override val editItemTitle: String
        get() = "Edit Buku"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_book)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
