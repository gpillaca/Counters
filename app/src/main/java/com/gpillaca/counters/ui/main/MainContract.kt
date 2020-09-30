package com.gpillaca.counters.ui.main

import com.gpillaca.counters.domain.Counter
import com.gpillaca.counters.ui.common.BaseContract

interface MainContract {
    interface View {
        fun show(counterUiModel: CounterUiModel)
    }

    interface Presenter : BaseContract.Presenter {
        fun loadCounters(forceUpdate: Boolean)
        fun incrementCounter(counter: Counter)
        fun decrementCounter(counter: Counter)
        fun deleteCounters(counters: List<Counter>)
    }
}