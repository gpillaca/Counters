package com.gpillaca.counters.ui.main

import com.gpillaca.counters.domain.Counter

enum class RetryAction {
    DELETE,
    LOAD
}

sealed class CounterUiModel {
    object Loading : CounterUiModel()
    data class Error(val title: String, val message: String, val retryAction: RetryAction) :
        CounterUiModel()

    data class Message(val title: String, val message: String) : CounterUiModel()
    data class Success(val counters: List<Counter>, val items: Int, val times: Int) :
        CounterUiModel()
}