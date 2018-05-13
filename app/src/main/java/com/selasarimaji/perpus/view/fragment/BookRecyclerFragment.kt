package com.selasarimaji.perpus.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.view.*
import com.selasarimaji.perpus.R
import kotlinx.android.synthetic.main.fragment_recycler.view.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.selasarimaji.perpus.CONTENT_TYPE_KEY
import com.selasarimaji.perpus.ContentType
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.view.activity.BookCreationActivity
import com.selasarimaji.perpus.view.activity.CategoryCreationActivity
import com.selasarimaji.perpus.view.activity.FragmentedActivity
import com.selasarimaji.perpus.view.adapter.ContentRecyclerAdapter
import com.selasarimaji.perpus.viewmodel.EditBookVM

class BookRecyclerFragment : BaseRecyclerFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(activity!!).get(EditBookVM::class.java)
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

    override fun setupButton(view: View){
        view.fabItem1.setImageResource(R.drawable.ic_category)
        view.fabItem2.setImageResource(R.drawable.ic_book)
        view.fabItem1.labelText = "Kategori"
        view.fabItem2.labelText = "Buku"


        view.fabItem1.setOnClickListener {
            val intent = Intent(context, CategoryCreationActivity::class.java)
            startActivity(intent)
        }

        view.fabItem2.setOnClickListener {
            val intent = Intent(context, BookCreationActivity::class.java)
            startActivity(intent)
        }
    }

    override fun setupRecycler(view: View){
        val adapter = ContentRecyclerAdapter<DataModel.Book>(ContentType.Book)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.recyclerView.layoutManager = layoutManager
        view.recyclerView.adapter = adapter

        view.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                val totalRemoteCount = viewModel.totalRemoteCount.value ?: totalItemCount
                if (lastVisiblePosition + thresholdItemCount >= totalItemCount
                        && totalItemCount < totalRemoteCount){
                    viewModel.loadMore()
                }
            }
        })

        viewModel.repo.fetchedData.observe(this, Observer {
            it?.let {
                adapter.setupNewData(it)
            }
        })
        viewModel.loadInitial()
    }
}
