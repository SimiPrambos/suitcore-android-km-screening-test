package com.suitcore.base.presenter

import androidx.annotation.StringRes

interface MvpView {

    fun showLoading(isBackPressedCancelable: Boolean = true, message: String? = null, currentPage: Int? = 1)

    fun showLoadingWithText(msg: String)

    fun showLoadingWithText(@StringRes msg: Int)

    fun hideLoading()

    fun showConfirmationDialog(message: String, confirmCallback: () -> Unit)

    fun showConfirmationSingleDialog(message: String, confirmCallback: () -> Unit)

    fun showConfirmationDialog(@StringRes message: Int, confirmCallback: () -> Unit)

    fun showAlertDialog(message: String)

    fun showAlertDialog(@StringRes message: Int)

}