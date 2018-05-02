package com.selasarimaji.perpus.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.view.activity.ContentCreationActivity
import kotlinx.android.synthetic.main.fragment_recycler.view.*
import android.support.v7.widget.LinearLayoutManager
import com.selasarimaji.perpus.CONTENT_TYPE_KEY
import com.selasarimaji.perpus.ContentType
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.repository.firebase.BookRepo
import com.selasarimaji.perpus.view.activity.FragmentedActivity
import com.selasarimaji.perpus.view.adapter.ContentRecyclerAdapter
import com.selasarimaji.perpus.viewmodel.EditBookVM

class BookRecyclerFragment : Fragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(activity!!).get(EditBookVM::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recycler, container, false).apply {
            setupButton(this)
            setupRecycler(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.book_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.app_bar_list -> {
                startActivity(Intent(context, FragmentedActivity::class.java).apply {
                    putExtra(CONTENT_TYPE_KEY, ContentType.Category)
                })
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupButton(view: View){
        val firstMenu = ContentType.Category
        val secondMenu = ContentType.Book
        view.fabItem1.setImageResource(R.drawable.ic_category)
        view.fabItem2.setImageResource(R.drawable.ic_book)
        view.fabItem1.labelText = "Kategori"
        view.fabItem2.labelText = "Buku"


        val intent = Intent(context, ContentCreationActivity::class.java)
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
        val adapter = ContentRecyclerAdapter<DataModel.Book>(ContentType.Book)
        view.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.recyclerView.adapter = adapter

        (viewModel.repo as BookRepo).fetchedData.observe(this, Observer {
            it?.let {
                adapter.setupNewData(it)
            }
        })
        viewModel.loadInitial()
    }
}
