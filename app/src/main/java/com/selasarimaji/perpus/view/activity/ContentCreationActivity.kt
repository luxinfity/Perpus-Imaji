package com.selasarimaji.perpus.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.view.fragment.content.BookCreationFragment
import com.selasarimaji.perpus.view.fragment.content.BorrowCreationFragment
import com.selasarimaji.perpus.view.fragment.content.CategoryCreationFragment
import com.selasarimaji.perpus.view.fragment.content.KidCreationFragment
import com.selasarimaji.perpus.viewmodel.EditBookVM
import com.selasarimaji.perpus.viewmodel.EditBorrowVM
import com.selasarimaji.perpus.viewmodel.EditCategoryVM
import com.selasarimaji.perpus.viewmodel.EditKidVM
import kotlinx.android.synthetic.main.layout_content_creation.*

class ContentCreationActivity : BaseNavigationActivity() {
    companion object {
        const val VIEW_TYPE_KEY = "VIEW_TYPE_KEY"
        fun createIntentToHere(context: Context, viewType: ViewType) =
            Intent(context, ContentCreationActivity::class.java).apply {
                putExtra(VIEW_TYPE_KEY, viewType)
            }
    }
    enum class ViewType {
        Category, Book, Kid, Borrow
    }

    private val viewType by lazy {
        if (intent.hasExtra(VIEW_TYPE_KEY)) {
            intent.getSerializableExtra(VIEW_TYPE_KEY) as ViewType
        }
        else {
            ViewType.Book
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_content_creation)
        setupToolbar()
        setupObservers(viewType)
        setupFragmentContent(viewType)
    }

    private fun setupToolbar(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupFragmentContent(viewType: ViewType){
        val fragment = when(viewType){
            ViewType.Category -> CategoryCreationFragment()
            ViewType.Book -> BookCreationFragment()
            ViewType.Kid -> KidCreationFragment()
            ViewType.Borrow -> BorrowCreationFragment()
        }

        supportFragmentManager.beginTransaction().replace(frameContainer.id, fragment).commit()
    }

    private fun setupObservers(viewType: ViewType){
        val viewModel = when(viewType){
            ViewType.Category -> ViewModelProviders.of(this).get(EditCategoryVM::class.java)
            ViewType.Book -> ViewModelProviders.of(this).get(EditBookVM::class.java)
            ViewType.Kid -> ViewModelProviders.of(this).get(EditKidVM::class.java)
            ViewType.Borrow -> ViewModelProviders.of(this).get(EditBorrowVM::class.java)
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
