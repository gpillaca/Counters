package com.gpillaca.counters.ui.main

import com.gpillaca.counters.R
import com.gpillaca.counters.data.repository.CounterRepository
import com.gpillaca.counters.domain.Counter
import com.gpillaca.counters.ui.common.OperationResults.Error
import com.gpillaca.counters.ui.common.OperationResults.Success
import com.gpillaca.counters.ui.common.Scope
import com.gpillaca.counters.util.AndroidHelper
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private val view: MainContract.View,
    private val counterRepository: CounterRepository
) : MainContract.Presenter, Scope by Scope.Impl() {

    init {
        onCreateScope()
    }

    override fun loadCounters() {
        launch {
            view.show(CounterUiModel.Loading)

            if (!AndroidHelper.hasNetworkConnection()) {
                sendErrorMessage()
                return@launch
            }

            when (val response = counterRepository.listCounters()) {
                is Success -> {
                    if (response.data.isEmpty()) {
                        view.show(
                            CounterUiModel.Message(
                                title = AndroidHelper.getString(R.string.no_counters_yet),
                                message = AndroidHelper.getString(R.string.no_counters_yet_message)
                            )
                        )
                        return@launch
                    }

                    view.show(
                        CounterUiModel.Success(
                            counters = response.data,
                            items = response.data.size,
                            times = countTimes(response.data)
                        )
                    )
                }
                is Error -> {
                    sendErrorMessage()
                }
            }
        }
    }

    override fun incrementCounter(id: String) {
        launch {
            when (val response = counterRepository.increment(id)) {
                is Success -> {
                    view.show(
                        CounterUiModel.Success(
                            counters = response.data,
                            items = response.data.size,
                            times = countTimes(response.data)
                        )
                    )
                }
                is Error -> {
                    sendErrorMessage()
                }
            }
        }
    }

    override fun decrementCounter(id: String) {
        launch {
            when (val response = counterRepository.decrement(id)) {
                is Success -> {
                    view.show(
                        CounterUiModel.Success(
                            counters = response.data,
                            items = response.data.size,
                            times = countTimes(response.data)
                        )
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

    private fun sendErrorMessage() {
        view.show(
            CounterUiModel.Error(
                title = AndroidHelper.getString(R.string.couldnt_load_the_counters),
                message = AndroidHelper.getString(R.string.couldnt_load_the_counters_message)
            )
        )
    }

    override fun onCreateScope() {
        createScope()
    }

    override fun onDestroyScope() {
        destroyScope()
    }
}