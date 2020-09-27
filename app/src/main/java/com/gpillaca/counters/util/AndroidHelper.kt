package com.gpillaca.counters.util

import android.content.Context
import android.net.ConnectivityManager

private const val DEFAULT_STRING = ""

object AndroidHelper {
    private var context: Context? = null

    fun init(context: Context) {
        this.context = context
    }

    fun hasNetworkConnection(): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo?.isConnected == true
    }

    fun getString(id: Int): String {
        return context?.getString(id) ?: DEFAULT_STRING
    }
}