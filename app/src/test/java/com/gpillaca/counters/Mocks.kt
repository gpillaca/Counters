package com.gpillaca.counters

import com.gpillaca.counters.domain.Counter

val mockedCounter = Counter("c1", "Cups of coffee", 0)

val mockedCounters = mutableListOf(
    Counter("c2", "Cupcakes eaten", 2),
    Counter("c3", "Day dreaming", 3),
    Counter("c4", "Times sneezed", 4)
)