package com.gpillaca.counters.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.gpillaca.counters.R
import com.gpillaca.counters.databinding.ActivityMainBinding
import com.gpillaca.counters.domain.Counter
import com.gpillaca.counters.ui.addcounter.AddCounterActivity
import com.gpillaca.counters.ui.main.CounterAction.*
import com.gpillaca.counters.ui.main.CounterUiModel.*
import com.gpillaca.counters.util.DialogHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val REQUEST_CODE_ACTIVITY_ADD_COUNTER = 0
private const val DEFAULT_NUMBERS_ITEMS = 0

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    MainContract.View,
    View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var counterAdapter: CounterAdapter
    private var counter: Counter? = null

    @Inject
    lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        initAdapter()
        initViews()
        presenter.loadCounters()
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
                presenter.loadCounters()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun resetToolbar() {
        supportActionBar?.title = ""
        binding.toolbar.title = ""
    }

    private fun showToolbar(value: Boolean) {
        binding.toolbar.visibility = if (value) View.VISIBLE else View.GONE
        binding.appBarLayout.visibility = if (value) View.VISIBLE else View.GONE
    }

    private fun showSearch(value: Boolean) {
        binding.layoutSearch.root.visibility = if (value) View.VISIBLE else View.GONE
    }

    private fun resetAdapter() {
        counterAdapter.clearSelection()
        counterAdapter.countersTemp = emptyList()
    }

    private fun initViews() {
        binding.toolbarButtonDelete.setOnClickListener(this)
        binding.toolbarButtonShare.setOnClickListener(this)
        binding.buttonAddCounter.setOnClickListener(this)
        binding.layoutSearch.root.setOnClickListener(this)
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.color_accent)
        binding.swipeRefreshLayout.setOnRefreshListener {
            presenter.loadCounters()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.editTextName.doOnTextChanged { text, _, _, count ->
            if (count > 0) {
                binding.imageButtonCancel.visibility = View.VISIBLE
            } else {
                binding.imageButtonCancel.visibility = View.GONE
            }

            counterAdapter.filter.filter(text)
        }
        binding.imageButtonBack.setOnClickListener(this)
        binding.imageButtonCancel.setOnClickListener(this)
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
                if (counters.isEmpty()) {
                    binding.textViewLabelNoResults.visibility = View.VISIBLE
                } else {
                    binding.textViewLabelNoResults.visibility = View.GONE
                }

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

    private fun showDialogCounter() {
        val dialog = DialogHelper(this).apply {
            this.title = R.string.couldnt_update_counter
            this.message = R.string.couldnt_update_counter_message
            cancelable = true
            onPositiveButton(R.string.retry) { presenter.loadCounters() }
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
                presenter.deleteCounters(counterAdapter.counters)
            }
            onNegativeButton(R.string.cancel) { dialog.dismiss() }
        }.create()
        dialog.show()
    }

    private fun plusCounter(counter: Counter) {
        presenter.incrementCounter(counter.id)
    }

    private fun lessCounter(counter: Counter) {
        presenter.decrementCounter(counter.id)
    }

    private fun deleteCounter(counter: Counter) {
        this.counter = counter
    }

    override fun show(counterUiModel: CounterUiModel) {
        showToolbar(false)
        showSearch(true)
        binding.viewMessageBackground.visibility = View.GONE
        binding.viewMessage.root.visibility = View.GONE
        binding.progressBar.visibility = if (counterUiModel is Loading) View.VISIBLE else View.GONE

        when (counterUiModel) {
            is Success -> {
                resetToolbar()
                resetAdapter()
                showCounters(counterUiModel.counters, counterUiModel.items, counterUiModel.times)
            }
            is Message -> {
                showMessageEmptyList(counterUiModel.title, counterUiModel.message)
            }
            is Error -> {
                showError(counterUiModel.title, counterUiModel.message, counterUiModel.retryAction)
            }
        }
    }

    private fun showError(title: String, message: String, retryAction: RetryAction) {
        binding.viewMessage.buttonRetry.tag = retryAction
        binding.viewMessage.buttonRetry.setOnClickListener(this)
        binding.viewMessage.textViewTitle.text = title
        binding.viewMessage.textViewMessage.text = message

        showViewMessage(value = true, activeButton = true)
    }

    private fun showMessageEmptyList(title: String, message: String) {
        counterAdapter.counters = emptyList()
        binding.viewMessage.textViewTitle.text = title
        binding.viewMessage.textViewMessage.text = message
        showNumbersOfItems()
        showViewMessage(true)
    }

    private fun showCounters(counters: List<Counter>, items: Int, times: Int) {
        showNumbersOfItems(items, times)
        binding.recyclerViewCounters.adapter = counterAdapter
        counterAdapter.counters = counters
    }

    private fun showNumbersOfItems(
        items: Int = DEFAULT_NUMBERS_ITEMS,
        times: Int = DEFAULT_NUMBERS_ITEMS
    ) {
        binding.textViewItems.text = getString(R.string.items, items.toString())
        binding.textViewTimes.text = getString(R.string.times, times.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroyScope()
    }

    private fun navigateToAddCounter() {
        val intent = Intent(this, AddCounterActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_ACTIVITY_ADD_COUNTER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_ACTIVITY_ADD_COUNTER &&
            resultCode == Activity.RESULT_OK
        ) {
            presenter.loadCounters()
        }
    }

    private fun showViewMessage(value: Boolean, activeButton: Boolean = false) {
        binding.viewMessage.root.visibility = if (value) View.VISIBLE else View.GONE
        binding.viewMessageBackground.visibility = if (value) View.VISIBLE else View.GONE
        binding.viewMessage.buttonRetry.visibility = if (activeButton) View.VISIBLE else View.GONE
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

    override fun onClick(view: View?) {
        val id = view?.id ?: return

        when (id) {
            R.id.buttonRetry -> {
                when (view.tag as RetryAction) {
                    RetryAction.LOAD -> {
                        presenter.loadCounters()
                    }
                    RetryAction.DELETE -> {
                        presenter.deleteCounters(counterAdapter.counters)
                    }
                }

                showViewMessage(false)
            }
            R.id.buttonAddCounter -> {
                navigateToAddCounter()
            }
            R.id.toolbarButtonDelete -> {
                if (counterAdapter.selectedItems.size() == DEFAULT_NUMBERS_ITEMS) {
                    warningDialog()
                    return
                }

                confirmDeleteMessage()
            }
            R.id.toolbarButtonShare -> {
                if (counterAdapter.selectedItems.size() == DEFAULT_NUMBERS_ITEMS) {
                    warningDialog()
                    return
                }

                shareCounters()
            }
            R.id.layoutSearch -> {
                binding.editTextName.setText("")
                binding.appBarLayout.visibility = View.VISIBLE
                binding.constraintLayoutSearch.visibility = View.VISIBLE
                binding.layoutSearch.root.visibility = View.GONE
                binding.editTextName.requestFocus()
            }
            R.id.imageButtonBack -> {
                binding.appBarLayout.visibility = View.GONE
                binding.layoutSearch.root.visibility = View.VISIBLE
                binding.constraintLayoutSearch.visibility = View.GONE
                binding.editTextName.setText("")
            }
            R.id.imageButtonCancel -> {
                binding.editTextName.setText("")
            }
        }
    }
}