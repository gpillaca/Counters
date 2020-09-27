package com.gpillaca.counters.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gpillaca.counters.R
import com.gpillaca.counters.databinding.ActivityMainBinding
import com.gpillaca.counters.domain.Counter
import com.gpillaca.counters.ui.main.CounterUiModel.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainContract.View, View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonAddCounter.setOnClickListener(this)
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
        binding.viewMessage.buttonRetry.setOnClickListener(this)

        binding.viewMessageBackground.visibility = View.VISIBLE
        binding.viewMessage.root.visibility = View.VISIBLE
        binding.viewMessage.buttonRetry.visibility = View.VISIBLE

        binding.viewMessage.textViewTitle.text = title
        binding.viewMessage.textViewMessage.text = message
    }

    private fun showMessage(title: String, message: String) {
        binding.viewMessageBackground.visibility = View.VISIBLE
        binding.viewMessage.root.visibility = View.VISIBLE
        binding.viewMessage.buttonRetry.visibility = View.GONE

        binding.viewMessage.textViewTitle.text = title
        binding.viewMessage.textViewMessage.text = message
    }

    private fun showCounters(counters: List<Counter>) {

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroyScope()
    }

    override fun onClick(view: View?) {
        val id = view?.id ?: return

        when (id) {
            R.id.buttonRetry -> {
            }
            R.id.buttonAddCounter -> {
            }
        }
    }
}