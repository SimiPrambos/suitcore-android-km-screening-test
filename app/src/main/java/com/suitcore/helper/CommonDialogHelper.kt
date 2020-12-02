package com.suitcore.helper

import android.content.Context
import android.view.Window
import androidx.appcompat.app.AlertDialog
import com.suitcore.R


/**
 * Created by @dodydmw19 on 01, December, 2020
 */

class CommonDialogHelper {
    companion object{
         fun showConfirmationDialog(context: Context, message: String, confirmCallback: () -> Unit) {
            val confirmDialog = AlertDialog.Builder(context)
                    .setMessage(message)
                    .setPositiveButton(R.string.txt_dialog_yes) { _, _ -> confirmCallback() }
                    .setNegativeButton(R.string.txt_dialog_no) { _, _ -> }
                    .create()

            confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            confirmDialog.show()
        }

         fun showConfirmationSingleDialog(context: Context, message: String, confirmCallback: () -> Unit) {
            val confirmDialog = AlertDialog.Builder(context)
                    .setMessage(message)
                    .setPositiveButton(R.string.txt_dialog_ok) { _, _ -> confirmCallback() }
                    .create()

            confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            confirmDialog.show()
        }

        fun showAlertDialog(context: Context, message: String) {
            val confirmDialog = AlertDialog.Builder(context)
                    .setMessage(message)
                    .setPositiveButton(R.string.txt_dialog_ok) { d, _ -> d.dismiss() }
                    .create()

            confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            confirmDialog.show()
        }
    }

}