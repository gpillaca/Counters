package com.gpillaca.counters.util

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gpillaca.counters.R

class DialogHelper(private val context: Context) {

    private val builder: AlertDialog.Builder = MaterialAlertDialogBuilder(context, R.style.AppTheme_Dialog)
    lateinit var dialog: AlertDialog

    var title = R.string.app_name
    lateinit var message: Any
    var cancelable = true

    fun create(): AlertDialog {
        val messageDialog = when (message) {
            is Int -> context.getString(message as Int)
            is String -> message.toString()
            else -> ""
        }
        dialog = builder
            .setTitle(title)
            .setMessage(messageDialog)
            .setCancelable(cancelable)
            .create()

        return dialog
    }

    fun onPositiveButton(textId: Int = R.string.ok, func: () -> Unit): AlertDialog.Builder? =
        builder.setPositiveButton(textId) { _: DialogInterface, _: Int ->
            func()
        }

    fun onNegativeButton(textId: Int = R.string.cancel, func: () -> Unit): AlertDialog.Builder? =
        builder.setNegativeButton(textId) { _: DialogInterface, _: Int ->
            func()
        }

    fun onCancelListener(func: () -> Unit): AlertDialog.Builder? =
        builder.setOnCancelListener {
            func()
        }
}