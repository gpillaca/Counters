package com.gpillaca.counters.util

import android.app.Activity
import android.graphics.Color
import android.os.Build

fun Activity.supportStatusBar() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        window.statusBarColor = Color.BLACK
    }
}