package com.gpillaca.counters.ui.addcounter

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gpillaca.counters.ui.common.OperationResults
import com.gpillaca.counters.ui.common.Scope
import com.gpillaca.counters.usecases.AddCounter
import com.gpillaca.counters.util.AndroidHelper
import kotlinx.coroutines.launch

class AddCounterViewModel @ViewModelInject constructor(
    private val androidHelper: AndroidHelper,
    private val addCounter: AddCounter
) : ViewModel(), Scope by Scope.Impl() {

    init {
        createScope()
    }

    private val _model = MutableLiveData<AddCounterUiModel>()
    val model: LiveData<AddCounterUiModel>
        get() {
            return _model
        }

    fun createCounter(title: String) {
        launch {
            _model.value = AddCounterUiModel.Loading

            when (addCounter.invoke(title)) {
                is OperationResults.Success -> {
                    if (!androidHelper.hasNetworkConnection()) {
                        _model.value = AddCounterUiModel.Error
                        return@launch
                    }

                    _model.value = AddCounterUiModel.Success
                }
                is OperationResults.Error -> {
                    _model.value = AddCounterUiModel.Error
                }
            }
        }
    }

    override fun onCleared() {
        destroyScope()
        super.onCleared()
    }
}