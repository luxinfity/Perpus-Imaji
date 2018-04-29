package com.selasarimaji.perpus.view.activity

import android.os.Bundle
import com.selasarimaji.perpus.R

class EditKidActivity : BaseContentCreationActivity() {
    override val newItemTitle: String
        get() = "Anak Baru"

    override val editItemTitle: String
        get() = "Edit Anak"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_kid)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
