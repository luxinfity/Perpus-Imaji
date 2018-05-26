package com.selasarimaji.perpus.view.fragment.content

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.viewmodel.BorrowVM

class BorrowInspectFragment : BaseInspectFragment() {

    override val viewModel by lazy {
        ViewModelProviders.of(activity!!).get(BorrowVM::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_borrow, container, false)
    }

    override fun setupView(){
    }

    override fun setupToolbar(){
//        viewModel.title.value = "Kategori"
    }
}
