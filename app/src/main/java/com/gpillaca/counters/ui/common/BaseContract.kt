package com.gpillaca.counters.ui.common

interface BaseContract {
    interface View
    interface Presenter {
        fun onCreateScope()
        fun onDestroyScope()
    }
}