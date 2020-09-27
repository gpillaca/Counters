package com.gpillaca.counters.data.mappers

import com.gpillaca.counters.domain.Counter as CounterDomain
import com.gpillaca.counters.data.server.Counter as CounterServer

fun CounterServer.toDomainCounter() = CounterDomain(
    id,
    title,
    count
)