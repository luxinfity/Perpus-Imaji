package com.selasarimaji.perpus.view.fragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.*
import kotlinx.android.synthetic.main.fragment_recycler.view.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.selasarimaji.perpus.ContentType
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.view.activity.CategoryCreationActivity
import com.selasarimaji.perpus.view.adapter.ContentRecyclerAdapter
import com.selasarimaji.perpus.viewmodel.EditCategoryVM

class CategoryRecyclerFragment : BaseRecyclerFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(activity!!).get(EditCategoryVM::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState).apply {
            viewModel.title.value = "Daftar Kategori"
        }
    }

    override fun setupButton(view: View){
        view.fabButton.visibility = View.VISIBLE
        view.fabMenu.visibility = View.GONE

        view.fabButton.setOnClickListener {
            val intent = Intent(context, CategoryCreationActivity::class.java)
            startActivityForResult(intent, CREATION_REQUEST_CODE)
        }
    }

    override fun setupRecycler(view: View){
        val adapter = ContentRecyclerAdapter<DataModel.Category>(ContentType.Category)
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
                if (it.isNotEmpty()) dismissLoading()
            }
        })
        viewModel.loadInitial()
    }

    override fun refresh(){
        super.refresh()
        viewModel.reload()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATION_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            refresh()
        }
    }
}
