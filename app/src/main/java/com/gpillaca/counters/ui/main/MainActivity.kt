package com.gpillaca.counters.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gpillaca.counters.R
import com.gpillaca.counters.databinding.ActivityMainBinding
import com.gpillaca.counters.domain.Counter
import com.gpillaca.counters.ui.addcounter.AddCounterActivity
import com.gpillaca.counters.ui.main.CounterAction.DELETE
import com.gpillaca.counters.ui.main.CounterAction.DIALOG
import com.gpillaca.counters.ui.main.CounterAction.LESS
import com.gpillaca.counters.ui.main.CounterAction.PLUS
import com.gpillaca.counters.ui.main.CounterUiModel.Error
import com.gpillaca.counters.ui.main.CounterUiModel.Loading
import com.gpillaca.counters.ui.main.CounterUiModel.Message
import com.gpillaca.counters.ui.main.CounterUiModel.Success
import com.gpillaca.counters.ui.main.CounterUiModel.Update
import com.gpillaca.counters.util.DialogHelper
import com.gpillaca.counters.util.KeyBoardUtil
import com.gpillaca.counters.util.supportStatusBar
import dagger.hilt.android.AndroidEntryPoint

private const val DEFAULT_NUMBERS_ITEMS = 0

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var counterAdapter: CounterAdapter
    private var counter: Counter? = null
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportStatusBar()
        initToolbar()
        initAdapter()
        initViews()

        viewModel.model.observe(this, Observer(::updateUi))
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_cancel)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        resetToolbar()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                resetToolbar()
                resetAdapter()
                viewModel.loadCounters(forceUpdate = false)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun resetToolbar() {
        supportActionBar?.title = ""
        binding.toolbar.title = ""
    }

    private fun showToolbar(value: Boolean) {
        binding.toolbar.isVisible = value
        binding.appBarLayout.isVisible = value
    }

    private fun showSearch(value: Boolean) {
        binding.layoutSearch.root.isVisible = value
    }

    private fun resetAdapter() {
        counterAdapter.clearSelection()
        counterAdapter.countersTemp = emptyList()
    }

    private fun initViews() {
        binding.toolbarButtonDelete.setOnClickListener {
            if (counterAdapter.selectedItems.size() == DEFAULT_NUMBERS_ITEMS) {
                warningDialog()
                return@setOnClickListener
            }

            confirmDeleteMessage()
        }

        binding.toolbarButtonShare.setOnClickListener {
            if (counterAdapter.selectedItems.size() == DEFAULT_NUMBERS_ITEMS) {
                warningDialog()
                return@setOnClickListener
            }

            shareCounters()
        }

        binding.buttonAddCounter.setOnClickListener {
            navigateToAddCounter()
        }

        binding.layoutSearch.root.setOnClickListener {
            binding.editTextName.setText("")
            binding.appBarLayout.isVisible = true
            binding.constraintLayoutSearch.isVisible = true
            binding.layoutSearch.root.isGone = true
            KeyBoardUtil(this).showSoftKeyboard(binding.editTextName)
        }

        binding.swipeRefreshLayout.setColorSchemeResources(R.color.color_accent)
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadCounters(forceUpdate = true)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.editTextName.doOnTextChanged { text, _, _, count ->
            binding.imageButtonCancel.isVisible = count > 0
            counterAdapter.filter.filter(text)
        }

        binding.imageButtonBack.setOnClickListener {
            KeyBoardUtil(this).hideSoftKeyboard()
            binding.appBarLayout.isGone = true
            binding.layoutSearch.root.isVisible = true
            binding.constraintLayoutSearch.isGone = true
            binding.editTextName.setText("")
            viewModel.loadCounters(false)
        }

        binding.imageButtonCancel.setOnClickListener {
            binding.editTextName.setText("")
        }
    }

    private fun initAdapter() {
        counterAdapter = CounterAdapter(
            onClickCounterListener = { counter, action ->
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
                    DIALOG -> {
                        showDialogCounter()
                    }
                }
            },
            onLongClickCounterListener = {
                val countItems = counterAdapter.selectedItems.size()
                binding.toolbar.title = getString(R.string.delete_title, countItems.toString())
                supportActionBar?.title = getString(R.string.delete_title, countItems.toString())

                if (countItems == DEFAULT_NUMBERS_ITEMS) {
                    showToolbar(false)
                    showSearch(true)
                } else {
                    showToolbar(true)
                    showSearch(false)
                }
            },
            lister = { counters ->
                showMessageNoResults(counters)

                var times = 0
                counters.forEach {
                    times += it.count
                }

                showNumbersOfItems(counters.size, times)
            }
        )

        binding.recyclerViewCounters.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewCounters.addItemDecoration(CounterItemDecorator())
    }

    private fun showMessageNoResults(counters: List<Counter>) {
        binding.textViewLabelNoResults.isVisible = counters.isEmpty()
    }

    private fun showDialogCounter() {
        val dialog = DialogHelper(this).apply {
            this.title = R.string.couldnt_update_counter
            this.message = R.string.couldnt_update_counter_message
            cancelable = true
            onPositiveButton(R.string.retry) { viewModel.loadCounters(forceUpdate = false) }
            onNegativeButton(R.string.dismiss) { dialog.dismiss() }
        }.create()
        dialog.show()
    }

    private fun confirmDeleteMessage() {
        val dialog = DialogHelper(this).apply {
            this.title = R.string.confirm_delete_message
            this.message = ""
            cancelable = true
            onPositiveButton(R.string.delete) {
                viewModel.deleteCounters(counterAdapter.counters)
            }
            onNegativeButton(R.string.cancel) { dialog.dismiss() }
        }.create()
        dialog.show()
    }

    private fun plusCounter(counter: Counter) {
        viewModel.incrementCounter(counter)
    }

    private fun lessCounter(counter: Counter) {
        viewModel.decrementCounter(counter)
    }

    private fun deleteCounter(counter: Counter) {
        this.counter = counter
    }

    private fun updateUi(counterUiModel: CounterUiModel) {
        binding.viewMessageBackground.isGone = true
        binding.viewMessage.root.isGone = true
        binding.progressBar.isVisible = counterUiModel is Loading

        when (counterUiModel) {
            is Success -> {
                showToolbar(false)
                showSearch(true)
                resetToolbar()
                resetAdapter()
                showMessageNoResults(counterUiModel.counters)
                showCounters(counterUiModel.counters, counterUiModel.items, counterUiModel.times)
            }
            is Message -> {
                showToolbar(false)
                showSearch(true)
                showMessageEmptyList(counterUiModel.title, counterUiModel.message)
            }
            is Error -> {
                showError(counterUiModel.title, counterUiModel.message, counterUiModel.retryAction)
            }
            is Update -> {
                showNumbersOfItems(counterUiModel.items, counterUiModel.times)
                counterAdapter.upDateCounter(counterUiModel.counter)
            }
        }
    }

    private fun showError(title: String, message: String, retryAction: RetryAction) {
        binding.viewMessage.buttonRetry.tag = retryAction
        binding.viewMessage.buttonRetry.setOnClickListener {
            when (it.tag as RetryAction) {
                RetryAction.LOAD -> {
                    viewModel.loadCounters(forceUpdate = true)
                }
                RetryAction.DELETE -> {
                    viewModel.deleteCounters(counterAdapter.counters)
                }
            }

            showViewMessage(false)
        }
        binding.viewMessage.textViewTitle.text = title
        binding.viewMessage.textViewMessage.text = message

        showViewMessage(value = true, activeButton = true)
    }

    private fun showMessageEmptyList(title: String, message: String) {
        counterAdapter.counters = mutableListOf()
        binding.viewMessage.textViewTitle.text = title
        binding.viewMessage.textViewMessage.text = message
        showNumbersOfItems()
        showViewMessage(true)
    }

    private fun showCounters(counters: List<Counter>, items: Int, times: Int) {
        showNumbersOfItems(items, times)
        binding.recyclerViewCounters.adapter = counterAdapter
        counterAdapter.counters = counters as MutableList<Counter>
    }

    private fun showNumbersOfItems(
        items: Int = DEFAULT_NUMBERS_ITEMS,
        times: Int = DEFAULT_NUMBERS_ITEMS
    ) {
        binding.textViewItems.text = getString(R.string.items, items.toString())
        binding.textViewTimes.text = getString(R.string.times, times.toString())
    }

    private val startForResult = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.loadCounters(forceUpdate = true)
        }
    }

    private fun navigateToAddCounter() {
        val intent = Intent(this, AddCounterActivity::class.java)
        startForResult.launch(intent)
    }

    private fun showViewMessage(value: Boolean, activeButton: Boolean = false) {
        binding.viewMessage.root.isVisible = value
        binding.viewMessageBackground.isVisible = value
        binding.viewMessage.buttonRetry.isVisible = activeButton
    }

    private fun shareCounters() {
        val message = counterAdapter.counters
            .filter { it.isSelected }
            .joinToString(", ") {
                "${it.count} x ${it.title}"
            }

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun warningDialog() {
        val dialog = DialogHelper(this).apply {
            this.message = R.string.select_one_item
            onPositiveButton(R.string.ok) {
                dialog.dismiss()
            }
        }.create()
        dialog.show()
    }
}