package com.selasarimaji.perpus.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import com.selasarimaji.perpus.R
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.selasarimaji.perpus.CONTENT_TYPE_KEY
import com.selasarimaji.perpus.ContentType
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.view.fragment.content.*
import com.selasarimaji.perpus.viewmodel.*
import kotlinx.android.synthetic.main.activity_content_inspect.*

class ContentInspectActivity : BaseNavigationActivity() {
    companion object {
        private const val DATA_CONTENT_KEY = "DATA_CONTENT_KEY"
        fun createIntentToHere(context: Context, contentType: ContentType, data: DataModel? = null) =
                Intent(context, ContentInspectActivity::class.java).apply {
                    putExtra(CONTENT_TYPE_KEY, contentType)
                    putExtra(DATA_CONTENT_KEY, data)
                }
    }

    private lateinit var fragmentInfo: BaseInspectFragment

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
        (intent.getSerializableExtra(DATA_CONTENT_KEY) as DataModel?).also {
            // pair <isEdit, isCreate>
            viewModel.editOrCreateMode.value = Pair(it == null, it == null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_inspect)

        viewModel.shouldShowProgressBar.observe(this, Observer {
            it?.run {
                progressBar.visibility = if (this) View.VISIBLE else View.GONE
            }
        })

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
        fragmentInfo = when(contentType){
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.edit_menu, menu)
        viewModel.editOrCreateMode.observe(this, Observer {
            it?.let {
                menu.findItem(R.id.app_bar_save).isVisible = it.first && !it.second
                menu.findItem(R.id.app_bar_edit).isVisible = !it.first && !it.second
                menu.findItem(R.id.app_bar_delete).isVisible = !it.first && !it.second
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.app_bar_edit -> {
                viewModel.editOrCreateMode.value = Pair(true, false)
                fragmentInfo.focusFirstText()
            }
            R.id.app_bar_save-> {
                viewModel.editOrCreateMode.value = Pair(false, false)
                fragmentInfo.clearFocus()
            }
            R.id.app_bar_delete-> {
                viewModel.editOrCreateMode.value = Pair(false, false)
                fragmentInfo.tryDeleteCurrentItem()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
