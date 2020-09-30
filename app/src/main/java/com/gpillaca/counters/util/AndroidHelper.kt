package com.gpillaca.counters.util

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject

private const val DEFAULT_STRING = ""

class AndroidHelper @Inject constructor(private val context: Context) {
    fun hasNetworkConnection(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo?.isConnected == true
    }

    fun getString(id: Int): String {
        return context.getString(id) ?: DEFAULT_STRING
    }
}