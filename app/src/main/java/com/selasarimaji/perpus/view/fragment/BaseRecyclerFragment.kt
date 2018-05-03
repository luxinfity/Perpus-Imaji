package com.selasarimaji.perpus.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.selasarimaji.perpus.R

abstract class BaseRecyclerFragment : Fragment() {
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
        }
    }

    abstract fun setupButton(view: View)

    abstract fun setupRecycler(view: View)
}
