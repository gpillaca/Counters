package com.gpillaca.counters.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gpillaca.counters.databinding.ActivityMainBinding
import com.gpillaca.counters.domain.Counter
import com.gpillaca.counters.ui.main.CounterUiModel.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter.loadCounters()
    }

    override fun show(counterUiModel: CounterUiModel) {
        binding.progressBar.visibility = if (counterUiModel is Loading) View.VISIBLE else View.GONE

        when (counterUiModel) {
            is Success -> showCounters(counterUiModel.counters)
            is Message -> showMessage(counterUiModel.title, counterUiModel.message)
            is Error -> showError(counterUiModel.title, counterUiModel.message)
        }
    }

    private fun showError(title: String, message: String) {

    }

    private fun showMessage(title: String, message: String) {

    }

    private fun showCounters(counters: List<Counter>) {

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroyScope()
    }
}