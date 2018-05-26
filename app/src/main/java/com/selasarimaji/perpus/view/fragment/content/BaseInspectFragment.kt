package com.selasarimaji.perpus.view.fragment.content

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.selasarimaji.perpus.viewmodel.BaseLoadingVM

abstract class BaseInspectFragment : Fragment() {
    abstract val viewModel : BaseLoadingVM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupToolbar()
    }

    protected abstract fun setupToolbar()
    protected abstract fun setupView()
}
