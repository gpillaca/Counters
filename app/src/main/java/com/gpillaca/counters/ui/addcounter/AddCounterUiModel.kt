package com.gpillaca.counters.ui.addcounter

sealed class AddCounterUiModel {
    object Loading : AddCounterUiModel()
    object Error : AddCounterUiModel()
    object Success : AddCounterUiModel()
}