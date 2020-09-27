package com.gpillaca.counters.data.server

data class Counter(val id: String, val title: String, val count: Int)
data class CounterDbResult(val counters: List<Counter>)