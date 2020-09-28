package com.gpillaca.counters.domain

data class Counter(val id: String, val title: String, val count: Int) {
    var isSelected: Boolean = false
}