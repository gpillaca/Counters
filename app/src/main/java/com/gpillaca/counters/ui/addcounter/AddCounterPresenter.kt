package com.gpillaca.counters.ui.addcounter

import com.gpillaca.counters.ui.common.OperationResults.Error
import com.gpillaca.counters.ui.common.OperationResults.Success
import com.gpillaca.counters.ui.common.Scope
import com.gpillaca.counters.usecases.AddCounter
import com.gpillaca.counters.util.AndroidHelper
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddCounterPresenter @Inject constructor(
    private val view: AddCounterContract.View,
    private val androidHelper: AndroidHelper,
    private val addCounter: AddCounter
) : AddCounterContract.Presenter, Scope by Scope.Impl() {

    init {
        onCreateScope()
    }

    override fun createCounter(title: String) {
        launch {
            view.show(AddCounterUiModel.Loading)

            when (addCounter.invoke(title)) {
                is Success -> {
                    if (!androidHelper.hasNetworkConnection()) {
                        view.show(AddCounterUiModel.Error)
                        return@launch
                    }

                    view.show(AddCounterUiModel.Success)
                }
                is Error -> {
                    view.show(AddCounterUiModel.Error)
                }
            }
        }
    }

    override fun onCreateScope() {
        createScope()
    }

    override fun onDestroyScope() {
        destroyScope()
    }
}