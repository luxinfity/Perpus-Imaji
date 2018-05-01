package com.selasarimaji.perpus.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.selasarimaji.perpus.CONTENT_TYPE_KEY
import com.selasarimaji.perpus.ContentType
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.view.fragment.RecyclerFabMenuFragment
import com.selasarimaji.perpus.viewmodel.EditBookVM
import com.selasarimaji.perpus.viewmodel.EditBorrowVM
import com.selasarimaji.perpus.viewmodel.EditCategoryVM
import com.selasarimaji.perpus.viewmodel.EditKidVM

import kotlinx.android.synthetic.main.activity_fragmented.*

class FragmentedActivity : BaseNavigationActivity() {

    private val contentType by lazy {
        intent.extras?.getSerializable(CONTENT_TYPE_KEY) ?: ContentType.Book
    }

    private val viewModel by lazy {
        when(contentType){
            ContentType.Category -> ViewModelProviders.of(this).get(EditCategoryVM::class.java)
            ContentType.Book -> ViewModelProviders.of(this).get(EditBookVM::class.java)
            ContentType.Borrow -> ViewModelProviders.of(this).get(EditBorrowVM::class.java)
            ContentType.Kid -> ViewModelProviders.of(this).get(EditKidVM::class.java)
            else -> ViewModelProviders.of(this).get(EditBookVM::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragmented)

        supportFragmentManager.beginTransaction().replace(frameContainer.id, RecyclerFabMenuFragment
                .instantiate(contentType as ContentType)).commit()

        viewModel.title.observe(this, Observer {
            supportActionBar?.title = it
        })
    }

}
