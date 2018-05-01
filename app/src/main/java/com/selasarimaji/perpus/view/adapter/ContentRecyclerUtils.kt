package com.selasarimaji.perpus.view.adapter

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.selasarimaji.perpus.ContentType
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.viewmodel.EditBookVM
import com.selasarimaji.perpus.viewmodel.EditBorrowVM
import com.selasarimaji.perpus.viewmodel.EditCategoryVM
import com.selasarimaji.perpus.viewmodel.EditKidVM
import android.support.v7.widget.LinearLayoutManager



abstract class BaseContentViewHolder(view: View) : RecyclerView.ViewHolder(view)

class BookViewHolder(view: View) : BaseContentViewHolder(view)

class ContentRecyclerAdapter(): RecyclerView.Adapter<BaseContentViewHolder>(){

    lateinit var fragment: Fragment
    lateinit var contentType: ContentType
    lateinit var recyclerView: RecyclerView

    val visibleThreshold = 7
    var total = 10

    private val viewModel by lazy {
        when(contentType){
            ContentType.Category -> ViewModelProviders.of(fragment).get(EditCategoryVM::class.java)
            ContentType.Book -> ViewModelProviders.of(fragment).get(EditBookVM::class.java)
            ContentType.Borrow -> ViewModelProviders.of(fragment).get(EditBorrowVM::class.java)
            ContentType.Kid -> ViewModelProviders.of(fragment).get(EditKidVM::class.java)
        }
    }

    constructor(fragment: Fragment, contentType: ContentType, recyclerView: RecyclerView) : this() {
        this.fragment = fragment
        this.contentType = contentType
        this.recyclerView = recyclerView
        
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val llm = recyclerView.layoutManager as LinearLayoutManager

                val lastVisibleItem = llm.findLastVisibleItemPosition()
                val totalItemCount = llm.itemCount

                if (totalItemCount <= lastVisibleItem + visibleThreshold) {
                    total += 10
                    notifyDataSetChanged()
                }
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseContentViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.layout_category, parent, false )
        return BookViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return total
    }

    override fun onBindViewHolder(holder: BaseContentViewHolder, position: Int) {

    }
}