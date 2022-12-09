package com.dot.muzo.util

import android.app.Activity
import android.view.View
import com.dot.muzo.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class Util {

    companion object{
        fun bottomNavActive(activity: Activity) {
            val views = activity.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            views.visibility = View.VISIBLE
        }

        fun bottomNavInActive(activity: Activity) {
            val views = activity.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            views.visibility = View.GONE
        }
    }
}