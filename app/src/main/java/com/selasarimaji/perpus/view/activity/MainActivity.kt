package com.selasarimaji.perpus.view.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.selasarimaji.perpus.R
import com.selasarimaji.perpus.view.BookFragment
import com.selasarimaji.perpus.view.InfoFragment
import com.selasarimaji.perpus.view.KidFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val bookFragment by lazy { BookFragment() }
    private val kidFragment by lazy { KidFragment() }
    private val infoFragment by lazy { InfoFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_book -> selectFragment(bookFragment)
                R.id.menu_anak -> selectFragment(kidFragment)
                R.id.menu_info -> selectFragment(infoFragment)
            }
            true
        }

        selectFragment(bookFragment)
    }

    private fun selectFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(frameContainer.id, fragment).commit()
    }
}
