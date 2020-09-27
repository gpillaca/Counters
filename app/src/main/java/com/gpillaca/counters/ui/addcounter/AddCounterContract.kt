package com.gpillaca.counters.ui.addcounter

import com.gpillaca.counters.ui.common.BaseContract

interface AddCounterContract {
    interface View {
        fun show(addCounterUiModel: AddCounterUiModel)
    }

    interface Presenter: BaseContract.Presenter {
        fun createCounter(title: String)
    }
}