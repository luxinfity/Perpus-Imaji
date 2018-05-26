package com.selasarimaji.perpus.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.selasarimaji.perpus.CONTENT_TYPE_KEY
import com.selasarimaji.perpus.ContentType
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.view.fragment.content.create.BookCreationFragment
import com.selasarimaji.perpus.view.fragment.content.create.BorrowCreationFragment
import com.selasarimaji.perpus.view.fragment.content.create.CategoryCreationFragment
import com.selasarimaji.perpus.view.fragment.content.create.KidCreationFragment
import com.selasarimaji.perpus.viewmodel.BookVM
import com.selasarimaji.perpus.viewmodel.BorrowVM
import com.selasarimaji.perpus.viewmodel.CategoryVM
import com.selasarimaji.perpus.viewmodel.KidVM
import kotlinx.android.synthetic.main.activity_content_creation.*

class ContentCreationActivity : BaseNavigationActivity() {
    companion object {
        fun createIntentToHere(context: Context, contentType: ContentType) =
            Intent(context, ContentCreationActivity::class.java).apply {
                putExtra(CONTENT_TYPE_KEY, contentType)
            }
    }

    private val contentType by lazy {
        if (intent.hasExtra(CONTENT_TYPE_KEY)) {
            intent.getSerializableExtra(CONTENT_TYPE_KEY) as ContentType
        }
        else {
            null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_creation)
        setupToolbar()
        contentType?.run {
            setupObservers(this)
            setupFragmentContent(this)
        }
    }

    private fun setupToolbar(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupFragmentContent(contentType: ContentType){
        val fragment = when(contentType){
            ContentType.Category -> CategoryCreationFragment()
            ContentType.Book -> BookCreationFragment()
            ContentType.Kid -> KidCreationFragment()
            ContentType.Borrow -> BorrowCreationFragment()
        }

        supportFragmentManager.beginTransaction().replace(frameContainer.id, fragment).commit()
    }

    private fun setupObservers(contentType: ContentType){
        val viewModel = when(contentType){
            ContentType.Category -> ViewModelProviders.of(this).get(CategoryVM::class.java)
            ContentType.Book -> ViewModelProviders.of(this).get(BookVM::class.java)
            ContentType.Kid -> ViewModelProviders.of(this).get(KidVM::class.java)
            ContentType.Borrow -> ViewModelProviders.of(this).get(BorrowVM::class.java)
        }

        viewModel.uploadingFlag.observe(this, Observer {
            it?.run {
                progressBar.visibility = if (this) View.VISIBLE else View.GONE
            }
        })

        viewModel.title.observe(this, Observer {
            it?.run {
                supportActionBar!!.title = it
            }
        })
    }
}
