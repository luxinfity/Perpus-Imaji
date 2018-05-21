package com.selasarimaji.perpus.view.fragment.content

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.viewmodel.BaseLoadingVM
import kotlinx.android.synthetic.main.activity_base_content_creation.*

abstract class BaseCreationFragment : Fragment() {
    abstract val viewModel : BaseLoadingVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_base_content_creation, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupToolbar()
        setupObserver()

        addButton.setOnClickListener {
            submitValue()
        }
    }

    protected abstract fun setupToolbar()
    protected abstract fun submitValue()
    protected abstract fun setupView()
    protected abstract fun setupObserver()
}
