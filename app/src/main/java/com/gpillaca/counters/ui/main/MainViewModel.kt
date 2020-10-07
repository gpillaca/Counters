package com.gpillaca.counters.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gpillaca.counters.R
import com.gpillaca.counters.domain.Counter
import com.gpillaca.counters.ui.common.OperationResults.Error
import com.gpillaca.counters.ui.common.OperationResults.Success
import com.gpillaca.counters.ui.common.Scope
import com.gpillaca.counters.usecases.DecrementCounter
import com.gpillaca.counters.usecases.DeleteCounter
import com.gpillaca.counters.usecases.GetCounters
import com.gpillaca.counters.usecases.IncrementCounter
import com.gpillaca.counters.util.AndroidHelper
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val androidHelper: AndroidHelper,
    private val getCounters: GetCounters,
    private val deleteCounter: DeleteCounter,
    private val incrementCounter: IncrementCounter,
    private val decrementCounter: DecrementCounter
) : ViewModel(), Scope by Scope.Impl() {

    init {
        createScope()
    }

    private var _model = MutableLiveData<CounterUiModel>()
    val model: LiveData<CounterUiModel>
        get() {
            if (_model.value == null) loadCounters()
            return _model
        }

    fun deleteCounters(counters: List<Counter>) {
        launch {
            if (!androidHelper.hasNetworkConnection()) {
                sendErrorMessage(action = RetryAction.DELETE)
                return@launch
            }

            val deleteCounters = counters.filter { it.isSelected }
            deleteCounters.forEach {
                deleteCounter(it)
            }
        }
    }

    private suspend fun deleteCounter(counter: Counter) {
        _model.value = CounterUiModel.Loading

        when (val response = deleteCounter.invoke(counter)) {
            is Success -> {
                if (response.data.isEmpty()) {
                    _model.value = CounterUiModel.Message(
                        title = androidHelper.getString(R.string.no_counters_yet),
                        message = androidHelper.getString(R.string.no_counters_yet_message)
                    )
                    return
                }

                _model.value = CounterUiModel.Success(
                    counters = response.data,
                    items = response.data.size,
                    times = countTimes(response.data)
                )
            }
            is Error -> {
                sendErrorMessage(action = RetryAction.DELETE)
            }
        }

    }

    fun loadCounters(forceUpdate: Boolean = false) {
        launch {
            _model.value = CounterUiModel.Loading

            if (!androidHelper.hasNetworkConnection() && forceUpdate) {
                sendErrorMessage(action = RetryAction.LOAD)
                return@launch
            }

            when (val response = getCounters.invoke(forceUpdate)) {
                is Success -> {
                    if (response.data.isEmpty()) {
                        _model.value = CounterUiModel.Message(
                            title = androidHelper.getString(R.string.no_counters_yet),
                            message = androidHelper.getString(R.string.no_counters_yet_message)
                        )
                        return@launch
                    }

                    _model.value = CounterUiModel.Success(
                        counters = response.data,
                        items = response.data.size,
                        times = countTimes(response.data)
                    )
                }
                is Error -> {
                    sendErrorMessage(action = RetryAction.LOAD)
                }
            }
        }
    }

    fun incrementCounter(counter: Counter) {
        launch {
            when (val response = incrementCounter.invoke(counter)) {
                is Success -> {
                    val newCounter: Counter = response.data.filter { it.id == counter.id }[0]
                    newCounter.position = counter.position
                    _model.value = CounterUiModel.Update(
                        counter = newCounter,
                        items = response.data.size,
                        times = countTimes(response.data)
                    )
                }
                is Error -> {
                    sendErrorMessage()
                }
            }
        }
    }

    fun decrementCounter(counter: Counter) {
        launch {
            when (val response = decrementCounter.invoke(counter)) {
                is Success -> {
                    val newCounter: Counter = response.data.filter { it.id == counter.id }[0]
                    newCounter.position = counter.position
                    _model.value = CounterUiModel.Update(
                        counter = newCounter,
                        items = response.data.size,
                        times = countTimes(response.data)
                    )
                }
                is Error -> {
                    sendErrorMessage()
                }
            }
        }
    }

    private fun countTimes(counters: List<Counter>): Int {
        var times = 0

        counters.forEach {
            times += it.count
        }

        return times
    }

    private fun sendErrorMessage(action: RetryAction = RetryAction.LOAD) {
        _model.value = CounterUiModel.Error(
            title = androidHelper.getString(R.string.couldnt_load_the_counters),
            message = androidHelper.getString(R.string.couldnt_load_the_counters_message),
            retryAction = action
        )
    }

    override fun onCleared() {
        destroyScope()
        super.onCleared()
    }
}