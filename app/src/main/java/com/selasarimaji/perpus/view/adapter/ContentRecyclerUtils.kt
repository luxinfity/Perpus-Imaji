package com.selasarimaji.perpus.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.selasarimaji.perpus.ContentType
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.capitalizeWords
import com.selasarimaji.perpus.model.DataModel
import kotlinx.android.synthetic.main.layout_book.view.*
import kotlinx.android.synthetic.main.layout_borrow.view.*
import kotlinx.android.synthetic.main.layout_category.view.*
import kotlinx.android.synthetic.main.layout_kid.view.*

abstract class BaseContentViewHolder(val view: View) : RecyclerView.ViewHolder(view)

class CategoryViewHolder(view: View) : BaseContentViewHolder(view){
    fun setupView(item: DataModel.Category) {
        view.categoryNameText.text = item.name.capitalizeWords()
        view.categoryDesc.text = item.description.capitalizeWords()
    }
}
class BookViewHolder(view: View) : BaseContentViewHolder(view){
    fun setupView(item: DataModel.Book) {
        view.bookNameText.text = item.name.capitalizeWords()
        view.bookDesc.text = item.author.capitalizeWords()
    }
}
class BorrowViewHolder(view: View) : BaseContentViewHolder(view){
    fun setupView(item: DataModel.Borrow) {
        view.borrowIdText.text = item.id.toUpperCase()
        view.borrowDescText.text = "Anak : ${item.idChild} \nBuku : ${item.idBook}"
    }
}
class KidViewHolder(view: View) : BaseContentViewHolder(view){
    fun setupView(item:DataModel.Kid) {
        view.kidNameText.text = item.name.capitalizeWords()
        view.kidDesc.text = "DoB: ${item.birthDate} Blok: ${item.address} " +
                "Gender: ${if(item.isMale) "Cowok" else "Cewek"}"
    }
}

class ContentRecyclerAdapter <T: DataModel> (private val contentType: ContentType,
                                             private val onClickListener: (T) -> Unit)
    : RecyclerView.Adapter<BaseContentViewHolder>(){

    private val items = mutableListOf<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseContentViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(when(contentType){
            ContentType.Category -> R.layout.layout_category
            ContentType.Book -> R.layout.layout_book
            ContentType.Kid -> R.layout.layout_kid
            ContentType.Borrow -> R.layout.layout_borrow
        }, parent, false)

        return when(contentType){
            ContentType.Category -> CategoryViewHolder(layout)
            ContentType.Book -> BookViewHolder(layout)
            ContentType.Kid -> KidViewHolder(layout)
            ContentType.Borrow -> BorrowViewHolder(layout)
        }
    }

    override fun getItemCount() = items.size


    override fun onBindViewHolder(holder: BaseContentViewHolder, position: Int) {
        when(contentType){
            ContentType.Category -> (holder as CategoryViewHolder).setupView(items[position] as DataModel.Category)
            ContentType.Book -> (holder as BookViewHolder).setupView(items[position] as DataModel.Book)
            ContentType.Kid -> (holder as KidViewHolder).setupView(items[position] as DataModel.Kid)
            ContentType.Borrow -> (holder as BorrowViewHolder).setupView(items[position] as DataModel.Borrow)
        }

        holder.view.setOnClickListener {
            onClickListener(items[position])
        }
    }

    fun setupNewData(newList: List<T>){
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}