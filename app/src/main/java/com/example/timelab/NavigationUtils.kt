package com.example.timelab

import android.app.Activity
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView

fun setupBottomNavigation(activity: Activity, selectedItemId: Int) {
    val navView = activity.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    navView.selectedItemId = selectedItemId

    navView.setOnItemSelectedListener { item ->
        if (item.itemId == selectedItemId) {
            return@setOnItemSelectedListener true
        }

        val intent = when (item.itemId) {
            R.id.menu_item_1 -> Intent(activity, MainActivity::class.java)
            R.id.menu_item_2 -> Intent(activity, CalendarActivity::class.java)
            R.id.menu_item_3 -> Intent(activity, MatrixActivity::class.java)
            else -> null
        }

        intent?.let {
            activity.startActivity(it)

            // Выбираем анимации в зависимости от направления
            val transition = when {
                selectedItemId == R.id.menu_item_1 && item.itemId == R.id.menu_item_2 -> Pair(R.anim.slide_in_right, R.anim.slide_out_left)
                selectedItemId == R.id.menu_item_2 && item.itemId == R.id.menu_item_3 -> Pair(R.anim.slide_in_right, R.anim.slide_out_left)
                selectedItemId == R.id.menu_item_3 && item.itemId == R.id.menu_item_2 -> Pair(R.anim.slide_in_left, R.anim.slide_out_right)
                selectedItemId == R.id.menu_item_2 && item.itemId == R.id.menu_item_1 -> Pair(R.anim.slide_in_left, R.anim.slide_out_right)
                selectedItemId == R.id.menu_item_1 && item.itemId == R.id.menu_item_3 -> Pair(R.anim.slide_in_right, R.anim.slide_out_left)
                selectedItemId == R.id.menu_item_3 && item.itemId == R.id.menu_item_1 -> Pair(R.anim.slide_in_left, R.anim.slide_out_right)
                else -> Pair(android.R.anim.fade_in, android.R.anim.fade_out)
            }

            activity.overridePendingTransition(transition.first, transition.second)
        }

        true
    }
}

