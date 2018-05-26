package com.selasarimaji.perpus.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import com.selasarimaji.perpus.R
import android.os.Bundle
import com.selasarimaji.perpus.ContentType
import com.selasarimaji.perpus.view.fragment.content.BookInspectFragment
import com.selasarimaji.perpus.view.fragment.content.BorrowInspectFragment
import com.selasarimaji.perpus.view.fragment.content.CategoryInspectFragment
import com.selasarimaji.perpus.view.fragment.content.KidInspectFragment
import com.selasarimaji.perpus.viewmodel.BookVM
import com.selasarimaji.perpus.viewmodel.BorrowVM
import com.selasarimaji.perpus.viewmodel.CategoryVM
import com.selasarimaji.perpus.viewmodel.KidVM
import kotlinx.android.synthetic.main.activity_content_inspect.*

class ContentInspectActivity : BaseNavigationActivity() {
    companion object {
        const val VIEW_TYPE_KEY = "VIEW_TYPE_KEY"
        fun createIntentToHere(context: Context, contentType: ContentType) =
                Intent(context, ContentInspectActivity::class.java).apply {
                    putExtra(VIEW_TYPE_KEY, contentType)
                }
    }

    private val contentType by lazy {
        if (intent.hasExtra(VIEW_TYPE_KEY)) {
            intent.getSerializableExtra(VIEW_TYPE_KEY) as ContentType
        }
        else {
            ContentType.Book
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_inspect)
        setupToolbar()
        setupObserversInfo(contentType)
        setupObserversDetail(contentType)
        setupFragmentContent(contentType)
    }

    private fun setupToolbar(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupFragmentContent(contentType: ContentType){
        val fragmentInfo = when(contentType){
            ContentType.Category -> CategoryInspectFragment()
            ContentType.Book -> BookInspectFragment()
            ContentType.Kid -> KidInspectFragment()
            ContentType.Borrow -> BorrowInspectFragment()
        }

//        val fragmentDetail = when(contentType){
//            ContentType.Category -> BookRecyclerFragment()
//            ContentType.Book -> KidRecyclerFragment()
//            ContentType.Kid -> BorrowRecyclerFragment()
//            ContentType.Borrow -> null
//        }

        supportFragmentManager.beginTransaction().replace(frameContentInfo.id, fragmentInfo).commit()
//        fragmentDetail?.run {
//            supportFragmentManager.beginTransaction().replace(frameContentDetail.id, this).commit()
//        }
    }

    private fun setupObserversInfo(contentType: ContentType){
        val viewModel = when(contentType){
            ContentType.Category -> ViewModelProviders.of(this).get(CategoryVM::class.java)
            ContentType.Book -> ViewModelProviders.of(this).get(BookVM::class.java)
            ContentType.Kid -> ViewModelProviders.of(this).get(KidVM::class.java)
            ContentType.Borrow -> ViewModelProviders.of(this).get(BorrowVM::class.java)
        }

        viewModel.title.observe(this, Observer {
            it?.run {
                supportActionBar!!.title = it
            }
        })
    }

    private fun setupObserversDetail(contentType: ContentType){
        val viewModel = when(contentType){
            ContentType.Category -> ViewModelProviders.of(this).get(CategoryVM::class.java)
            ContentType.Book -> ViewModelProviders.of(this).get(BookVM::class.java)
            ContentType.Kid -> ViewModelProviders.of(this).get(KidVM::class.java)
            ContentType.Borrow -> ViewModelProviders.of(this).get(BorrowVM::class.java)
        }
    }
}
