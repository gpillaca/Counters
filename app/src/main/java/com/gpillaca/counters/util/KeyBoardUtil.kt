package com.gpillaca.counters.util

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager

class KeyBoardUtil(private val activity: Activity) {
    fun hideSoftKeyboard() {
        if (activity.currentFocus != null) {
            val inputMethodManager: InputMethodManager =
                activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
        }
    }

    fun showSoftKeyboard(view: View) {
        val inputMethodManager: InputMethodManager =
            activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        inputMethodManager.showSoftInput(view, 0)
    }
}