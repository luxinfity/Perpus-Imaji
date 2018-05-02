package com.selasarimaji.perpus.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.view.activity.ContentCreationActivity
import kotlinx.android.synthetic.main.fragment_recycler.view.*
import android.support.v7.widget.LinearLayoutManager
import com.selasarimaji.perpus.CONTENT_TYPE_KEY
import com.selasarimaji.perpus.ContentType
import com.selasarimaji.perpus.model.DataModel
import com.selasarimaji.perpus.view.adapter.ContentRecyclerAdapter
import com.selasarimaji.perpus.viewmodel.EditCategoryVM

class CategoryRecyclerFragment : Fragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(activity!!).get(EditCategoryVM::class.java)
    }

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
            viewModel.title.value = "Daftar Kategori"
        }
    }

    private fun setupButton(view: View){
        view.fabButton.visibility = View.VISIBLE
        view.fabMenu.visibility = View.GONE
        val mainMenu = ContentType.Category

        val intent = Intent(context, ContentCreationActivity::class.java)
        view.fabButton.setOnClickListener {
            intent.putExtra(CONTENT_TYPE_KEY, mainMenu)
            startActivity(intent)
        }
    }

    private fun setupRecycler(view: View){
        val adapter = ContentRecyclerAdapter<DataModel.Category>(ContentType.Category)

        view.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.recyclerView.adapter = adapter

        viewModel.repo.fetchedData.observe(this, Observer {
            it?.let {
                adapter.setupNewData(it)
            }
        })
        viewModel.loadInitial()
    }
}
