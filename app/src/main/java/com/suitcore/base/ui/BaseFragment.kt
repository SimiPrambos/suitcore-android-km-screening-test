package com.suitcore.base.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.suitcore.base.presenter.MvpView
import com.suitcore.base.ui.recyclerview.BaseRecyclerView
import com.suitcore.helper.CommonLoadingDialog

abstract class BaseFragment : Fragment(), MvpView {

    private var mContext: Context? = null
    private lateinit var mBaseActivity: BaseActivity
    private var mInflater: LayoutInflater? = null
    private var mCommonLoadingDialog: CommonLoadingDialog? = null
    protected abstract val resourceLayout: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
        if (context is BaseActivity) {
            mBaseActivity = mContext as BaseActivity
            mInflater = LayoutInflater.from(mBaseActivity)
        } else {
            throw ClassCastException("The activity is not child of BaseActivity")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return mInflater?.inflate(resourceLayout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onViewReady(savedInstanceState)
    }

    protected abstract fun onViewReady(savedInstanceState: Bundle?)

    protected fun showToast(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }

    fun goToActivity(actDestination: Class<out Activity>, data: Bundle?, clearIntent: Boolean, isFinish: Boolean) {
        mBaseActivity.goToActivity(actDestination, data, clearIntent, isFinish)
    }

    fun goToActivity(resultCode: Int, actDestination: Class<out Activity>,data: Bundle?) {
        mBaseActivity.goToActivity(resultCode, actDestination, data)
    }

    override fun showLoading(isBackPressedCancelable: Boolean, message: String?, currentPage: Int?) {
        if (currentPage == 1) {
            mBaseActivity.showLoading(isBackPressedCancelable, message)
        }
    }

    override fun showLoadingWithText(msg: String) {
        showLoading(message = msg)
    }

    override fun showLoadingWithText(msg: Int) {
        showLoading(message = getString(msg))
    }

    override fun hideLoading() {
        mCommonLoadingDialog?.dismiss()
    }

    override fun showConfirmationDialog(message: String, confirmCallback: () -> Unit) {
        mBaseActivity.showConfirmationDialog(message, confirmCallback)
    }

    override fun showConfirmationSingleDialog(message: String, confirmCallback: () -> Unit) {
        mBaseActivity.showConfirmationSingleDialog(message, confirmCallback)
    }

    override fun showConfirmationDialog(message: Int, confirmCallback: () -> Unit) {
        mBaseActivity.showConfirmationDialog(message, confirmCallback)
    }

    override fun showAlertDialog(message: String) {
        mBaseActivity.showAlertDialog(message)
    }

    override fun showAlertDialog(message: Int) {
        mBaseActivity.showAlertDialog(message)
    }

    fun finishLoad(recycler: BaseRecyclerView?) {
        mBaseActivity.finishLoad(recycler)
    }

    fun clearRecyclerView(recyclerView: BaseRecyclerView?) {
        mBaseActivity.clearRecyclerView(recyclerView)
    }

}

