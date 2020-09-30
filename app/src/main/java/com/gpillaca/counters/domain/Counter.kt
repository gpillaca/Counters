package com.gpillaca.counters.domain

data class Counter(val id: String, val title: String, var count: Int) {
    var isSelected: Boolean = false
    var position: Int = -1
}