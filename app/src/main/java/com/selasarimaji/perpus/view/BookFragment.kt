package com.selasarimaji.perpus.view

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.selasarimaji.perpus.view.activity.EditBookActivity
import com.selasarimaji.perpus.view.activity.EditCategoryActivity
import com.selasarimaji.perpus.R
import kotlinx.android.synthetic.main.fragment_book.view.*

class BookFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_book, container, false).apply {
            setupButton(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.book_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupButton(view: View){
        view.newBookFab.setOnClickListener {
            startActivity(Intent(context, EditBookActivity::class.java))
        }

        view.newCategoryFab.setOnClickListener {
            startActivity(Intent(context, EditCategoryActivity::class.java))
        }
    }
}
