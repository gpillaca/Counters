package com.gpillaca.counters.ui.addcounter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gpillaca.counters.ui.common.OperationResults
import com.gpillaca.counters.usecases.AddCounter
import com.gpillaca.counters.util.AndroidHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCounterViewModel @Inject constructor(
    private val androidHelper: AndroidHelper,
    private val addCounter: AddCounter
) : ViewModel() {

    private val _model = MutableLiveData<AddCounterUiModel>()
    val model: LiveData<AddCounterUiModel>
        get() {
            return _model
        }

    fun createCounter(title: String) {
        viewModelScope.launch {
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
}