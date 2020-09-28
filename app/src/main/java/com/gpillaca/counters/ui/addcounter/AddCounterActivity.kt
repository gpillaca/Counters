package com.gpillaca.counters.ui.addcounter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.gpillaca.counters.R
import com.gpillaca.counters.databinding.ActivityAddCounterBinding
import com.gpillaca.counters.ui.addcounter.AddCounterUiModel.*
import com.gpillaca.counters.ui.example.ExampleCounterActivity
import com.gpillaca.counters.util.DialogHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val REQUEST_CODE_ACTIVITY_EXAMPLE = 0

@AndroidEntryPoint
class AddCounterActivity : AppCompatActivity(), AddCounterContract.View, View.OnClickListener {

    private lateinit var binding: ActivityAddCounterBinding
    private var alertDialog: AlertDialog? = null

    @Inject
    lateinit var presenter: AddCounterContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCounterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    override fun show(addCounterUiModel: AddCounterUiModel) {
        if (addCounterUiModel is Loading) View.VISIBLE else View.GONE

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
        binding.toolbarButtonSave.setOnClickListener(this)
        binding.textViewExample.setOnClickListener(this)
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
        if (isVisible) {
            binding.toolbarProgressBar.visibility = View.GONE
            binding.toolbarButtonSave.visibility = View.VISIBLE
        } else {
            binding.toolbarProgressBar.visibility = View.VISIBLE
            binding.toolbarButtonSave.visibility = View.GONE
        }
    }

    override fun onClick(view: View?) {
        val id = view?.id ?: return

        when (id) {
            R.id.textViewExample -> {
                navigateToExamples()
            }
            R.id.toolbarButtonSave -> {
                saveCounter()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_ACTIVITY_EXAMPLE && resultCode == Activity.RESULT_OK) {
            val counterName = data?.getStringExtra(ExampleCounterActivity.PARAM_COUNTER_NAME) ?: ""
            binding.textInputEditTextName.setText(counterName)
        }
    }

    private fun navigateToExamples() {
        val intent = Intent(this, ExampleCounterActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_ACTIVITY_EXAMPLE)
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
        presenter.createCounter(name)
    }

    override fun onDestroy() {
        presenter.onDestroyScope()
        super.onDestroy()
    }
}