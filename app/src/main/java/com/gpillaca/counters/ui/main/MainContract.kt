package com.gpillaca.counters.ui.main

interface MainContract {
    interface View {
        fun show(counterUiModel: CounterUiModel)
    }

    interface Presenter {
        fun onDestroyScope()
        fun loadCounters()
    }
}