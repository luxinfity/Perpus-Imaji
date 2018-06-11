package com.selasarimaji.perpus.view.fragment.recycler

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.Toast
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.viewmodel.InspectVM
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_recycler.*
import kotlinx.android.synthetic.main.fragment_recycler.view.*
import java.util.concurrent.TimeUnit

abstract class BaseRecyclerFragment : Fragment() {
    companion object {
        const val CREATION_REQUEST_CODE = 1
    }

    val thresholdItemCount = 3
    protected val viewModelInspect by lazy {
        ViewModelProviders.of(activity!!).get(InspectVM::class.java)
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
            ptrLayout.isRefreshing = true
            ptrLayout.setOnRefreshListener {
                refresh()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.content_menu, menu)

        RxSearchView.queryTextChanges(menu.findItem(R.id.app_bar_search).actionView as SearchView)
                .skip(1)
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    viewModelInspect.queryString.value = it.toString()
                }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showLoading(){
        ptrLayout.isRefreshing = true
    }

    fun dismissLoading(){
        ptrLayout.isRefreshing = false
    }

    abstract fun setupButton(view: View)

    abstract fun setupRecycler(view: View)

    open fun refresh(filterMap: Map<String, String>? = null){
        showLoading()
    }
}
