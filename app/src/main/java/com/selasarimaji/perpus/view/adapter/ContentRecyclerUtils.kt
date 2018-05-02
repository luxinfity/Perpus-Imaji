package com.selasarimaji.perpus.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.selasarimaji.perpus.ContentType
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.model.DataModel

abstract class BaseContentViewHolder(view: View) : RecyclerView.ViewHolder(view)

class BookViewHolder(view: View) : BaseContentViewHolder(view)

class ContentRecyclerAdapter <T: DataModel> (private val contentType: ContentType)
    : RecyclerView.Adapter<BaseContentViewHolder>(){

    private val items = mutableListOf<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseContentViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(when(contentType){
            ContentType.Category -> R.layout.layout_category
            ContentType.Book -> R.layout.layout_book
            ContentType.Kid -> R.layout.layout_kid
            ContentType.Borrow -> R.layout.layout_borrow
        }, parent, false)
        return BookViewHolder(layout)
    }

    override fun getItemCount() = items.size


    override fun onBindViewHolder(holder: BaseContentViewHolder, position: Int) {

    }

    fun setupNewData(newList: List<T>){
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}