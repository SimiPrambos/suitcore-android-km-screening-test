package com.suitcore.base.presenter

import androidx.annotation.StringRes

interface MvpView {

    fun showLoading(isBackPressedCancelable: Boolean = true, message: String? = null)

    fun showLoadingWithText(msg: String)

    fun showLoadingWithText(@StringRes msg: Int)

    fun hideLoading()

    fun showConfirmationDialog(message: String, confirmCallback: () -> Unit)

    fun showConfirmationSingleDialog(message: String, confirmCallback: () -> Unit)

    fun showConfirmationDialog(@StringRes message: Int, confirmCallback: () -> Unit)

    fun showAlertDialog(message: String)

    fun showAlertDialog(@StringRes message: Int)

    //    Custom Dialog
    fun showDialogLoading(dismiss: Boolean = false, message: String?)
    fun showDialogAlert(title: String?, message: String, confirmCallback: () -> Unit?={}, drawableImage: Int?=null)
    fun showDialogConfirmation(title: String?, message: String, confirmCallback: () -> Unit?={}, cancelCallback: ()-> Unit? = {}, drawableImage: Int?=null)
    fun showDialogCustomLayout(dismiss: Boolean, resourceLayout: Int, confirmCallback: () -> Unit?={})
}