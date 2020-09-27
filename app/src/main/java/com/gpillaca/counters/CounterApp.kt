package com.gpillaca.counters

import android.app.Application
import com.gpillaca.counters.util.AndroidHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CounterApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidHelper.init(this)
    }
}