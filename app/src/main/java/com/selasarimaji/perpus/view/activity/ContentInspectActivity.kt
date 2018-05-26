package com.selasarimaji.perpus.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import com.selasarimaji.perpus.R
import android.os.Bundle
import com.selasarimaji.perpus.view.fragment.content.BookInspectFragment
import com.selasarimaji.perpus.view.fragment.content.BorrowInspectFragment
import com.selasarimaji.perpus.view.fragment.content.CategoryInspectFragment
import com.selasarimaji.perpus.view.fragment.content.KidInspectFragment
import com.selasarimaji.perpus.view.fragment.recycler.BookRecyclerFragment
import com.selasarimaji.perpus.view.fragment.recycler.BorrowRecyclerFragment
import com.selasarimaji.perpus.view.fragment.recycler.CategoryRecyclerFragment
import com.selasarimaji.perpus.view.fragment.recycler.KidRecyclerFragment
import com.selasarimaji.perpus.viewmodel.EditBookVM
import com.selasarimaji.perpus.viewmodel.EditBorrowVM
import com.selasarimaji.perpus.viewmodel.EditCategoryVM
import com.selasarimaji.perpus.viewmodel.EditKidVM
import kotlinx.android.synthetic.main.activity_content_inspect.*

class ContentInspectActivity : BaseNavigationActivity() {
    companion object {
        const val VIEW_TYPE_KEY = "VIEW_TYPE_KEY"
        fun createIntentToHere(context: Context, viewType: BaseNavigationActivity.ViewType) =
                Intent(context, ContentCreationActivity::class.java).apply {
                    putExtra(VIEW_TYPE_KEY, viewType)
                }
    }

    private val viewType by lazy {
        if (intent.hasExtra(VIEW_TYPE_KEY)) {
            intent.getSerializableExtra(VIEW_TYPE_KEY) as BaseNavigationActivity.ViewType
        }
        else {
            BaseNavigationActivity.ViewType.Book
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_inspect)
        setupToolbar()
        setupObserversInfo(viewType)
        setupObserversDetail(viewType)
        setupFragmentContent(viewType)
    }

    private fun setupToolbar(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupFragmentContent(viewType: ViewType){
        val fragmentInfo = when(viewType){
            ViewType.Category -> CategoryInspectFragment()
            ViewType.Book -> BookInspectFragment()
            ViewType.Kid -> KidInspectFragment()
            ViewType.Borrow -> BorrowInspectFragment()
        }

//        val fragmentDetail = when(viewType){
//            ViewType.Category -> BookRecyclerFragment()
//            ViewType.Book -> KidRecyclerFragment()
//            ViewType.Kid -> BorrowRecyclerFragment()
//            ViewType.Borrow -> null
//        }

        supportFragmentManager.beginTransaction().replace(frameContentInfo.id, fragmentInfo).commit()
//        fragmentDetail?.run {
//            supportFragmentManager.beginTransaction().replace(frameContentDetail.id, this).commit()
//        }
    }

    private fun setupObserversInfo(viewType: ViewType){
        val viewModel = when(viewType){
            ViewType.Category -> ViewModelProviders.of(this).get(EditCategoryVM::class.java)
            ViewType.Book -> ViewModelProviders.of(this).get(EditBookVM::class.java)
            ViewType.Kid -> ViewModelProviders.of(this).get(EditKidVM::class.java)
            ViewType.Borrow -> ViewModelProviders.of(this).get(EditBorrowVM::class.java)
        }

        viewModel.title.observe(this, Observer {
            it?.run {
                supportActionBar!!.title = it
            }
        })
    }

    private fun setupObserversDetail(viewType: ViewType){
        val viewModel = when(viewType){
            ViewType.Category -> ViewModelProviders.of(this).get(EditCategoryVM::class.java)
            ViewType.Book -> ViewModelProviders.of(this).get(EditBookVM::class.java)
            ViewType.Kid -> ViewModelProviders.of(this).get(EditKidVM::class.java)
            ViewType.Borrow -> ViewModelProviders.of(this).get(EditBorrowVM::class.java)
        }
    }
}
