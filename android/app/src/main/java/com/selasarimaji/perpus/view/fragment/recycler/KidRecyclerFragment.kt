package com.selasarimaji.perpus.view.fragment.recycler

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.view.*
import kotlinx.android.synthetic.main.fragment_recycler.view.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.selasarimaji.perpus.ContentType
import com.selasarimaji.perpus.model.RepoDataModel
import com.selasarimaji.perpus.view.activity.ContentInspectActivity
import com.selasarimaji.perpus.view.adapter.ContentRecyclerAdapter
import com.selasarimaji.perpus.viewmodel.content.KidVM
import kotlinx.android.synthetic.main.fragment_recycler.*

class KidRecyclerFragment : BaseRecyclerFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(activity!!).get(KidVM::class.java)
    }

    private val adapter by lazy {
        ContentRecyclerAdapter<RepoDataModel.Kid>(ContentType.Kid) {
            context?.run {
                val intent = ContentInspectActivity.createIntentToHere(this, ContentType.Kid, it)
                startActivityForResult(intent, CREATION_REQUEST_CODE)
            }
        }
    }

    override fun setupButton(view: View){
        view.fabButton.setOnClickListener {
            context?.let {
                val intent = ContentInspectActivity.createIntentToHere(it, ContentType.Kid)
                startActivityForResult(intent, CREATION_REQUEST_CODE)
            }
        }
    }

    override fun setupRecycler(view: View){
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
                    viewModel.loadMore(viewModel.filterMap)
                }
            }
        })
        viewModel.isLoading.observe(this, Observer {
            ptrLayout.isRefreshing = it ?: false
        })
        viewModel.repo.fetchedData.observe(this, Observer {
            emptyText.visibility = if (it != null && it.isNotEmpty()) View.GONE else View.VISIBLE
            it?.let {
                adapter.setupNewData(it)
                if (it.isNotEmpty()) dismissLoading()
            }
        })
        viewModelInspect.editOrCreateMode.observe(this, Observer {
            fabButton.visibility = if (it?.first != true) View.VISIBLE else View.GONE
        })
        viewModelInspect.queryString.observe(this, Observer {
            onSearch(it ?: "")
        })
        viewModel.loadInitial()
    }

    override fun refresh(){
        super.refresh()
        viewModel.reload()
    }

    override fun onSearch(query: String) {
        super.onSearch(query)
        // direct result
        viewModel.repo.fetchedData.value?.filter { !it.name.contains(query) }?.map {
            // remove item that doesn't contain the name
            viewModel.repo.deleteLocalItem(it)
        }

        // remote result
        val filterMap = if (query.isNotEmpty()) mapOf("name" to query) else null
        viewModel.filterMap = filterMap
        viewModel.loadInitial(filterMap)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATION_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            refresh()
        }
    }
}
