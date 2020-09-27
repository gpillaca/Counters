package com.gpillaca.counters.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gpillaca.counters.R
import com.gpillaca.counters.databinding.ActivityMainBinding
import com.gpillaca.counters.domain.Counter
import com.gpillaca.counters.ui.main.CounterAction.*
import com.gpillaca.counters.ui.main.CounterUiModel.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainContract.View, View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var counterAdapter: CounterAdapter

    @Inject
    lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonAddCounter.setOnClickListener(this)
        initAdapter()
        presenter.loadCounters()
    }

    private fun initAdapter() {
        counterAdapter = CounterAdapter { counter, action ->
            when (action) {
                PLUS -> {
                    plusCounter(counter)
                }
                LESS -> {
                    lessCounter(counter)
                }
                DELETE -> {
                    deleteCounter(counter)
                }
            }
        }
    }

    private fun plusCounter(counter: Counter) {
        //TODO Implement function
        Toast.makeText(this, "${counter.title} PLUS", Toast.LENGTH_SHORT).show()
    }

    private fun lessCounter(counter: Counter) {
        //TODO Implement function
        Toast.makeText(this, "${counter.title} LESS", Toast.LENGTH_SHORT).show()
    }

    private fun deleteCounter(counter: Counter) {
        //TODO Implement function
        Toast.makeText(this, "${counter.title} DELETE", Toast.LENGTH_SHORT).show()
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
        binding.recyclerViewCounters.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewCounters.addItemDecoration(CounterItemDecorator())
        counterAdapter.counters = counters
        binding.recyclerViewCounters.adapter = counterAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroyScope()
    }

    private fun navigateToAddCounter() {
        //TODO Implement function
    }

    private fun hideViewMessage() {
        binding.viewMessage.root.visibility = View.GONE
        binding.viewMessageBackground.visibility = View.GONE
        binding.viewMessage.buttonRetry.visibility = View.GONE
    }

    override fun onClick(view: View?) {
        val id = view?.id ?: return

        when (id) {
            R.id.buttonRetry -> {
                hideViewMessage()
                presenter.loadCounters()
            }
            R.id.buttonAddCounter -> {
                navigateToAddCounter()
            }
        }
    }
}