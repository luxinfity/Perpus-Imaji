package com.selasarimaji.perpus.view.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.view.fragment.recycler.BookRecyclerFragment
import com.selasarimaji.perpus.view.fragment.InfoFragment
import com.selasarimaji.perpus.view.fragment.recycler.BorrowRecyclerFragment
import com.selasarimaji.perpus.view.fragment.recycler.CategoryRecyclerFragment
import com.selasarimaji.perpus.view.fragment.recycler.KidRecyclerFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val categoryFragment by lazy {
        CategoryRecyclerFragment()
    }
    private val bookFragment by lazy {
        BookRecyclerFragment()
    }
    private val kidFragment by lazy {
        KidRecyclerFragment()
    }
    private val borrowFragment by lazy {
        BorrowRecyclerFragment()
    }
    private val infoFragment by lazy { InfoFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_category -> selectFragment(categoryFragment)
                R.id.menu_book -> selectFragment(bookFragment)
                R.id.menu_anak -> selectFragment(kidFragment)
                R.id.menu_borrow -> selectFragment(borrowFragment)
                R.id.menu_info -> selectFragment(infoFragment)
            }
            true
        }

        selectFragment(categoryFragment)
    }

    private fun selectFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(frameContainer.id, fragment).commit()
    }
}
