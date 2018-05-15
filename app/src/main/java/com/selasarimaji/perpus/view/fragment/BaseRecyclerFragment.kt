package com.selasarimaji.perpus.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.selasarimaji.perpus.R
import kotlinx.android.synthetic.main.fragment_recycler.*
import kotlinx.android.synthetic.main.fragment_recycler.view.*

abstract class BaseRecyclerFragment : Fragment() {
    companion object {
        const val CREATION_REQUEST_CODE = 1
    }

    val thresholdItemCount = 3

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

    private fun showLoading(){
        ptrLayout.isRefreshing = true
    }

    fun dismissLoading(){
        ptrLayout.isRefreshing = false
    }

    abstract fun setupButton(view: View)

    abstract fun setupRecycler(view: View)

    open fun refresh(){
        showLoading()
    }
}
