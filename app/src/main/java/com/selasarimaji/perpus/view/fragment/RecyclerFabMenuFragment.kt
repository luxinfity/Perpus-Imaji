package com.selasarimaji.perpus.view.fragment

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.view.activity.ContentCreationActivity
import kotlinx.android.synthetic.main.fragment_recycler_fab_menu.view.*
import android.support.v7.widget.LinearLayoutManager
import com.selasarimaji.perpus.CONTENT_TYPE_KEY
import com.selasarimaji.perpus.ContentType
import com.selasarimaji.perpus.view.activity.FragmentedActivity
import com.selasarimaji.perpus.view.adapter.ContentRecyclerAdapter
import com.selasarimaji.perpus.viewmodel.EditBookVM
import com.selasarimaji.perpus.viewmodel.EditBorrowVM
import com.selasarimaji.perpus.viewmodel.EditCategoryVM
import com.selasarimaji.perpus.viewmodel.EditKidVM


class RecyclerFabMenuFragment : Fragment() {
    companion object {
        fun instantiate(contentType: ContentType) : RecyclerFabMenuFragment {
            return RecyclerFabMenuFragment().apply {
                val args = Bundle()
                args.putSerializable(CONTENT_TYPE_KEY, contentType)
                arguments = args
            }
        }
    }

    private val contentType by lazy {
        arguments?.getSerializable(CONTENT_TYPE_KEY) ?: ContentType.Book
    }

    private val viewModel by lazy {
        when(contentType){
            ContentType.Category -> ViewModelProviders.of(activity!!).get(EditCategoryVM::class.java)
            ContentType.Book -> ViewModelProviders.of(activity!!).get(EditBookVM::class.java)
            ContentType.Borrow -> ViewModelProviders.of(activity!!).get(EditBorrowVM::class.java)
            ContentType.Kid -> ViewModelProviders.of(activity!!).get(EditKidVM::class.java)
            else -> ViewModelProviders.of(activity!!).get(EditBookVM::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recycler_fab_menu, container, false).apply {
            setupButton(this)
            setupRecycler(this)
            setupTitle()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        val menuRes = when (contentType) {
            ContentType.Book -> R.menu.book_menu
            ContentType.Kid -> R.menu.kid_menu
            else -> null
        }
        menuRes?.let {
            inflater?.inflate(it, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.app_bar_list -> {
                startActivity(Intent(context, FragmentedActivity::class.java).apply {
                    putExtra(CONTENT_TYPE_KEY, when(contentType){
                        ContentType.Book -> ContentType.Category
                        else -> ContentType.Borrow
                    })
                })
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupTitle(){
        when(contentType){
            ContentType.Category -> viewModel.title.value = "Daftar Kategori"
            ContentType.Borrow -> viewModel.title.value = "Daftar Pinjam"
        }

    }

    private fun setupButton(view: View){
        var mainMenu = ContentType.Category
        var firstMenu = ContentType.Category
        var secondMenu = ContentType.Book

        when (contentType) {
            ContentType.Category -> {
                mainMenu = ContentType.Category
                view.fabButton.visibility = View.VISIBLE
                view.fabMenu.visibility = View.GONE
            }
            ContentType.Borrow ->{
                mainMenu = ContentType.Borrow
                view.fabButton.visibility = View.VISIBLE
                view.fabMenu.visibility = View.GONE
            }
            ContentType.Book -> {
                firstMenu = ContentType.Category
                secondMenu = ContentType.Book
                view.fabItem1.setImageResource(R.drawable.ic_category)
                view.fabItem2.setImageResource(R.drawable.ic_book)
                view.fabItem1.labelText = "Kategori"
                view.fabItem2.labelText = "Buku"
            }
            ContentType.Kid -> {
                firstMenu = ContentType.Borrow
                secondMenu = ContentType.Kid
                view.fabItem1.setImageResource(R.drawable.ic_borrow)
                view.fabItem2.setImageResource(R.drawable.ic_kid)
                view.fabItem1.labelText = "Peminjaman"
                view.fabItem2.labelText = "Anak"
            }
        }

        val intent = Intent(context, ContentCreationActivity::class.java)
        view.fabButton.setOnClickListener {
            intent.putExtra(CONTENT_TYPE_KEY, mainMenu)
            startActivity(intent)
        }
        view.fabItem1.setOnClickListener {
            intent.putExtra(CONTENT_TYPE_KEY, firstMenu)
            startActivity(intent)
        }

        view.fabItem2.setOnClickListener {
            intent.putExtra(CONTENT_TYPE_KEY, secondMenu)
            startActivity(intent)
        }
    }

    private fun setupRecycler(view: View){
        view.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.recyclerView.adapter = ContentRecyclerAdapter(this, ContentType.Book, view.recyclerView)
    }
}
