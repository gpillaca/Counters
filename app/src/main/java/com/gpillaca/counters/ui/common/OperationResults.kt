package com.gpillaca.counters.ui.common

sealed class OperationResults<out T> {
    data class Success<T>(val data: List<T>) : OperationResults<T>()
    data class Error @JvmOverloads constructor(val exception: Exception, val message: String = "") : OperationResults<Nothing>()
}