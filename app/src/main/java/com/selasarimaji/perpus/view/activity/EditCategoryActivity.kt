package com.selasarimaji.perpus.view.activity

import android.os.Bundle
import com.selasarimaji.perpus.R
import kotlinx.android.synthetic.main.activity_edit_category.*

class EditCategoryActivity : BaseContentCreationActivity() {

    override val newItemTitle: String
        get() = "Kategori Baru"

    override val editItemTitle: String
        get() = "Edit Kategori"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_category)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
