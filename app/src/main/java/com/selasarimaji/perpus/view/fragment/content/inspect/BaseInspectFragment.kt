package com.selasarimaji.perpus.view.fragment.content.inspect

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.selasarimaji.perpus.viewmodel.InspectVM

abstract class BaseInspectFragment : Fragment() {
    val viewModel by lazy {
        ViewModelProviders.of(activity!!).get(InspectVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupToolbar()
    }

    protected abstract fun setupToolbar()
    protected abstract fun setupView()
}
