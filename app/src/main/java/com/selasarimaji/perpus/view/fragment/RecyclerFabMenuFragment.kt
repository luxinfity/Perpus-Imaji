package com.selasarimaji.perpus.view.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.view.activity.ContentCreationActivity
import kotlinx.android.synthetic.main.fragment_recycler_fab_menu.view.*

class RecyclerFabMenuFragment : Fragment() {

    companion object {
        const val CONTENT_TYPE_KEY = "CONTENT_TYPE_KEY"

        fun intantiate(contentType: ContentType) : RecyclerFabMenuFragment {
            return RecyclerFabMenuFragment().apply {
                val args = Bundle()
                args.putSerializable(CONTENT_TYPE_KEY, contentType)
                arguments = args
            }
        }
    }

    enum class ContentType {
        Book, Kid
    }

    private val contentType by lazy {
        arguments?.getSerializable(CONTENT_TYPE_KEY) ?: ContentType.Book
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recycler_fab_menu, container, false).apply {
            setupButton(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        val menuRes = when (contentType) {
            ContentType.Book -> R.menu.book_menu
            ContentType.Kid -> R.menu.kid_menu
            else -> R.menu.book_menu
        }
        inflater?.inflate(menuRes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupButton(view: View){
        var firstMenu = ContentCreationActivity.ContentType.Category
        var secondMenu = ContentCreationActivity.ContentType.Book

        when (contentType) {
            ContentType.Book -> {
                firstMenu = ContentCreationActivity.ContentType.Category
                secondMenu = ContentCreationActivity.ContentType.Book
                view.fabItem1.setImageResource(R.drawable.ic_category)
                view.fabItem2.setImageResource(R.drawable.ic_book)
                view.fabItem1.labelText = "Kategori"
                view.fabItem2.labelText = "Buku"
            }
            ContentType.Kid -> {
                firstMenu = ContentCreationActivity.ContentType.Borrow
                secondMenu = ContentCreationActivity.ContentType.Kid
                view.fabItem1.setImageResource(R.drawable.ic_borrow)
                view.fabItem2.setImageResource(R.drawable.ic_kid)
                view.fabItem1.labelText = "Peminjaman"
                view.fabItem2.labelText = "Anak"
            }
        }

        val intent = Intent(context, ContentCreationActivity::class.java)
        view.fabItem1.setOnClickListener {
            intent.putExtra(ContentCreationActivity.CONTENT_TYPE_KEY, firstMenu)
            startActivity(intent)
        }

        view.fabItem2.setOnClickListener {
            intent.putExtra(ContentCreationActivity.CONTENT_TYPE_KEY, secondMenu)
            startActivity(intent)
        }
    }
}
