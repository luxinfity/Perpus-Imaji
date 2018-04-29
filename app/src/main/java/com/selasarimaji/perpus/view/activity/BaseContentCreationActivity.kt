package com.selasarimaji.perpus.view.activity

import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v4.app.TaskStackBuilder
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

abstract class BaseContentCreationActivity : AppCompatActivity() {
    companion object {
        const val ACTIVITY_TYPE_KEY = "ACTIVITY_TYPE_KEY"
        const val ACTIVITY_TYPE_NEW = 0
        const val ACTIVITY_TYPE_EDIT = 1
    }

    open val newItemTitle = ""
    open val editItemTitle = ""

    val activityType by lazy {
        intent?.extras?.getInt(ACTIVITY_TYPE_KEY, ACTIVITY_TYPE_NEW) ?: ACTIVITY_TYPE_NEW
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTitle()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                val upIntent = NavUtils.getParentActivityIntent(this)
                if (NavUtils.shouldUpRecreateTask(this, upIntent!!)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities()
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupTitle() {
        when (activityType) {
            ACTIVITY_TYPE_NEW -> if (newItemTitle.isNotEmpty()) supportActionBar?.title = newItemTitle
            ACTIVITY_TYPE_EDIT -> if (editItemTitle.isNotEmpty()) supportActionBar?.title = editItemTitle
        }
    }
}