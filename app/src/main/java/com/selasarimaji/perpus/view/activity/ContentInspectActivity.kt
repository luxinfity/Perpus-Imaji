package com.selasarimaji.perpus.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import com.selasarimaji.perpus.R
import android.os.Bundle
import com.selasarimaji.perpus.CONTENT_TYPE_KEY
import com.selasarimaji.perpus.ContentType
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.view.fragment.content.inspect.BookInspectFragment
import com.selasarimaji.perpus.view.fragment.content.inspect.BorrowInspectFragment
import com.selasarimaji.perpus.view.fragment.content.inspect.CategoryInspectFragment
import com.selasarimaji.perpus.view.fragment.content.inspect.KidInspectFragment
import com.selasarimaji.perpus.viewmodel.*
import kotlinx.android.synthetic.main.activity_content_inspect.*

class ContentInspectActivity : BaseNavigationActivity() {
    companion object {
        private const val DATA_CONTENT_KEY = "DATA_CONTENT_KEY"
        fun createIntentToHere(context: Context, contentType: ContentType, data: DataModel) =
                Intent(context, ContentInspectActivity::class.java).apply {
                    putExtra(CONTENT_TYPE_KEY, contentType)
                    putExtra(DATA_CONTENT_KEY, data)
                }
    }

    val viewModel by lazy {
        ViewModelProviders.of(this).get(InspectVM::class.java)
    }

    private val contentType by lazy {
        if (intent.hasExtra(CONTENT_TYPE_KEY)) {
            intent.getSerializableExtra(CONTENT_TYPE_KEY) as ContentType
        }
        else {
            null
        }
    }

    private val data by lazy {
        if (intent.hasExtra(DATA_CONTENT_KEY)) {
            intent.getSerializableExtra(DATA_CONTENT_KEY) as DataModel
        } else {
            null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_inspect)
        setupToolbar()
        contentType?.run {
            setupObserversInfo(this)
            setupObserversDetail(this)
            setupFragmentContent(this)

            viewModel.setSelectedItem(data)
        }
    }

    private fun setupToolbar(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel.title.observe(this, Observer {
            supportActionBar?.title = it
        })
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
