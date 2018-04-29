package com.selasarimaji.perpus.view.activity

import android.os.Bundle
import com.selasarimaji.perpus.R

class EditBorrowActivity : BaseContentCreationActivity() {

    override val newItemTitle: String
        get() = "Peminjaman Baru"

    override val editItemTitle: String
        get() = "Edit Peminjaman"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_borrow)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
