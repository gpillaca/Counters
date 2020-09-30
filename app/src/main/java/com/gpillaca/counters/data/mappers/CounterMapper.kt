package com.gpillaca.counters.data.mappers

import com.gpillaca.counters.data.database.Counter as CounterDataBase
import com.gpillaca.counters.domain.Counter as CounterDomain
import com.gpillaca.counters.data.server.Counter as CounterServer

fun CounterServer.toDomainCounter() = CounterDomain(
    id,
    title ?: "",
    count ?: 0
)

fun CounterDataBase.toDomainCounter(): CounterDomain {
    val counter = CounterDomain(id, title, count)
    counter.isSelected = isSelected
    return counter
}

fun CounterDomain.toDatabaseCounter(): CounterDataBase {
    val counter = CounterDataBase(id, title, count)
    counter.isSelected = isSelected
    return counter
}