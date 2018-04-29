package com.selasarimaji.perpus.view.activity

import android.os.Bundle
import com.selasarimaji.perpus.R

class EditKidActivity : BaseNavigationActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_kid)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
