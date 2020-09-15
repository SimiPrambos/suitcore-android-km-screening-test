package com.suitcore.helper

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.suitcore.R
import kotlinx.android.synthetic.main.dialog_loading.view.*

class CommonLoadingDialog : DialogFragment() {

    private lateinit var message: String

    companion object {
        const val TAG: String = "dialog_common_loading"
        const val IS_BACK_PRESSED_CANCELABLE = "is_back_pressed_cancelable"
        const val CUSTOM_MESSAGE = "custom_message"

        private fun newInstance(backPressedCancelable: Boolean, msg: String? = null): CommonLoadingDialog {
            val commonLoadingDialog = CommonLoadingDialog()
            val bundle = Bundle()
            bundle.putBoolean(IS_BACK_PRESSED_CANCELABLE, backPressedCancelable)
            bundle.putString(CUSTOM_MESSAGE, msg)
            commonLoadingDialog.arguments = bundle
            return commonLoadingDialog
        }

        fun createLoaderDialog(isBackPressedCancelable: Boolean = true, msg: String? = null): CommonLoadingDialog {
            return newInstance(isBackPressedCancelable, msg)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getString(CUSTOM_MESSAGE)?.let {
            message = it
        } ?: run {
            context.let {
                message = context.getString(R.string.txt_dialog_loading)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.dialog_loading, container, false)
        view.tvLoading.text = message
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        if (isCancelableOnBackPressed()) {
            dialog.setOnKeyListener { _: DialogInterface, keyCode: Int, keyEvent: KeyEvent ->
                onBackPressed(keyCode, keyEvent)
            }
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    private fun onBackPressed(keyCode: Int, keyEvent: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_UP) {
            dismiss()
            return true
        }
        return false
    }

    private fun isCancelableOnBackPressed() : Boolean {
        return arguments?.getBoolean(IS_BACK_PRESSED_CANCELABLE, false) ?: false
    }
}