package com.selasarimaji.perpus.view

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.view.activity.EditBorrowActivity
import com.selasarimaji.perpus.view.activity.EditKidActivity
import kotlinx.android.synthetic.main.fragment_borrow.view.*


class KidFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_borrow, container, false).apply {
            setupButton(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.kid_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupButton(view: View){
        view.newKidFab.setOnClickListener {
            startActivity(Intent(context, EditKidActivity::class.java))
        }

        view.newBorrowFab.setOnClickListener {
            startActivity(Intent(context, EditBorrowActivity::class.java))
        }
    }
}
