package com.gpillaca.counters.ui.addcounter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.gpillaca.counters.R
import com.gpillaca.counters.databinding.ActivityAddCounterBinding
import com.gpillaca.counters.ui.addcounter.AddCounterUiModel.Error
import com.gpillaca.counters.ui.addcounter.AddCounterUiModel.Loading
import com.gpillaca.counters.ui.addcounter.AddCounterUiModel.Success
import com.gpillaca.counters.ui.example.ExampleCounterActivity
import com.gpillaca.counters.util.DialogHelper
import com.gpillaca.counters.util.supportStatusBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCounterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCounterBinding
    private var alertDialog: AlertDialog? = null
    private val viewModel: AddCounterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCounterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportStatusBar()
        initView()

        viewModel.model.observe(this, Observer(::updateUi))
    }

    private fun updateUi(addCounterUiModel: AddCounterUiModel) {
        binding.toolbarProgressBar.isVisible = addCounterUiModel is Loading

        when (addCounterUiModel) {
            is Success -> {
                setResult(Activity.RESULT_OK)
                finish()
            }
            is Error -> {
                toolbarButtonSave(isVisible = true)
                showDialog()
            }
        }
    }

    private fun showDialog() {
        alertDialog = DialogHelper(this).apply {
            this.title = R.string.couldnt_load_the_counters
            this.message = R.string.couldnt_load_the_counters_message
            onPositiveButton {
                alertDialog?.dismiss()
            }
        }.create()
        alertDialog?.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initView() {
        binding.toolbarButtonSave.setOnClickListener {
            saveCounter()
        }
        binding.textViewExample.setOnClickListener {
            navigateToExamples()
        }
        binding.textInputEditTextName.requestFocus()
        initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_cancel)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun toolbarButtonSave(isVisible: Boolean) {
        binding.toolbarButtonSave.isVisible = isVisible
    }

    private val startForResult = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val counterName = result.data?.getStringExtra(ExampleCounterActivity.PARAM_COUNTER_NAME) ?: ""
            binding.textInputEditTextName.setText(counterName)
        }
    }

    private fun navigateToExamples() {
        val intent = Intent(this, ExampleCounterActivity::class.java)
        startForResult.launch(intent)
    }

    private fun saveCounter() {
        val name: String = binding.textInputLayoutName.editText?.text.toString().trim() ?: return

        if (name.isEmpty()) {
            binding.textInputLayoutName.error = binding.textInputEditTextName.context.getString(
                R.string.add_counter_name
            )
            return
        }

        toolbarButtonSave(isVisible = false)
        viewModel.createCounter(name)
    }
}