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
import com.selasarimaji.perpus.viewmodel.content.BorrowVM
import kotlinx.android.synthetic.main.fragment_recycler.*

class BorrowRecyclerFragment : BaseRecyclerFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(activity!!).get(BorrowVM::class.java)
    }

    override fun setupButton(view: View){
        view.fabButton.setOnClickListener {
            context?.let {
                val intent = ContentInspectActivity.createIntentToHere(it, ContentType.Borrow)
                startActivityForResult(intent, CREATION_REQUEST_CODE)
            }
        }
    }

    override fun setupRecycler(view: View){
        val adapter = ContentRecyclerAdapter<RepoDataModel.Borrow>(ContentType.Borrow){
            context?.run {
                val intent = ContentInspectActivity.createIntentToHere(this, ContentType.Borrow, it)
                startActivityForResult(intent, CREATION_REQUEST_CODE)
            }
        }
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
            it?.map {
                val data = it
                viewModel.repoBookVal.getRealNameOfId(data.idBook){
                    val bookName = it ?: ""
                    viewModel.repoKidVal.getRealNameOfId(data.idChild){
                        val childName = it ?: ""

                        adapter.updateData(data.copy(idBook = bookName,
                                idChild = childName).apply {
                            id = data.id
                        })
                    }
                }
            }

            it?.let {
                adapter.setupNewData(it)
                if (it.isNotEmpty()) dismissLoading()
            }
        })

        viewModelInspect.editOrCreateMode.observe(this, Observer {
            fabButton.visibility = if (it?.first != true) View.VISIBLE else View.GONE
        })
        viewModel.loadInitial()
    }

    override fun refresh(filterMap: Map<String, String>?){
        super.refresh(filterMap)
        viewModel.reload(filterMap)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATION_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            refresh()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // override to not make any menu
    }
}
