package com.suitcore.base.ui

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import com.suitcore.R
import com.suitcore.base.presenter.MvpView
import com.suitcore.base.ui.recyclerview.BaseRecyclerView
import com.suitcore.helper.CommonLoadingDialog


abstract class BaseActivity : AppCompatActivity(), MvpView {

    private var toolBar: Toolbar? = null
    private var mInflater: LayoutInflater? = null
    private var mActionBar: ActionBar? = null
    private var mCommonLoadingDialog: CommonLoadingDialog? = null
    private var activityIntent: Intent? = null

    private val baseFragmentManager: FragmentManager
        get() = super.getSupportFragmentManager()

    protected abstract val resourceLayout: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(resourceLayout)
        mInflater = LayoutInflater.from(this)
        onViewReady(savedInstanceState)
    }

    protected fun setupToolbar(baseToolbar: Toolbar, needHomeButton: Boolean) {
        baseToolbar.title = ""
        setupToolbar(baseToolbar, needHomeButton, null)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupToolbar(baseToolbar: Toolbar, needHomeButton: Boolean, onClickListener: View.OnClickListener?) {

        toolBar = baseToolbar
        setSupportActionBar(baseToolbar)
        mActionBar = supportActionBar
        if (mActionBar != null) {
            mActionBar!!.setHomeButtonEnabled(true)
            supportActionBar!!.setDisplayHomeAsUpEnabled(needHomeButton)
            supportActionBar!!.setDisplayShowTitleEnabled(true)
        }

        if (onClickListener != null)
            baseToolbar.setNavigationOnClickListener(onClickListener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setTitle(title: Int) {
        super.setTitle(title)
        if (mActionBar != null)
            mActionBar!!.title = getString(title)
    }

    fun changeProgressBarColor(color: Int, progressBar: ProgressBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            progressBar.indeterminateDrawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            @Suppress("DEPRECATION")
            progressBar.indeterminateDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }

    private fun clearActivity() {
        activityIntent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activityIntent?.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        activityIntent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        activityIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    fun goToActivity(actDestination: Class<out Activity>, data: Bundle?, clearIntent: Boolean, isFinish: Boolean) {

        activityIntent = Intent(this, actDestination)

        if (clearIntent) {
            clearActivity()
        }

        data?.let { activityIntent?.putExtras(data) }

        startActivity(activityIntent)

        if (isFinish) {
            finish()
        }
    }

    fun goToActivity(resultCode: Int, actDestination: Class<out Activity>, data: Bundle?) {
        activityIntent = Intent(this, actDestination)

        data?.let { activityIntent?.putExtras(data) }

        startActivityForResult(activityIntent, resultCode)

    }

    override fun onBackPressed() {
        if (baseFragmentManager.backStackEntryCount > 0) {
            baseFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    protected fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected abstract fun onViewReady(savedInstanceState: Bundle?)

    override fun showLoading(isBackPressedCancelable: Boolean, message: String?, currentPage: Int?) {
        if (currentPage == 1) {
            mCommonLoadingDialog?.let {
                hideLoading()
            }
            mCommonLoadingDialog = CommonLoadingDialog.createLoaderDialog(msg = message)
            mCommonLoadingDialog?.show(supportFragmentManager, CommonLoadingDialog.TAG)
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
        val confirmDialog = AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(R.string.txt_dialog_yes) { _, _ -> confirmCallback() }
                .setNegativeButton(R.string.txt_dialog_no) { _, _ -> }
                .create()

        confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        confirmDialog.show()
    }

    override fun showConfirmationSingleDialog(message: String, confirmCallback: () -> Unit) {
        val confirmDialog = AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(R.string.txt_dialog_ok) { _, _ -> confirmCallback() }
                .create()

        confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        confirmDialog.show()
    }

    override fun showConfirmationDialog(message: Int, confirmCallback: () -> Unit) {
        val stringMessage = getString(message)
        showConfirmationDialog(stringMessage, confirmCallback)
    }

    override fun showAlertDialog(message: String) {
        val confirmDialog = AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(R.string.txt_dialog_ok) { d, _ -> d.dismiss() }
                .create()

        confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        confirmDialog.show()
    }

    override fun showAlertDialog(message: Int) {
        val stringMessage = getString(message)
        showAlertDialog(stringMessage)
    }

    fun finishLoad(recycler: BaseRecyclerView?) {
        recycler?.let {
            it.completeRefresh()
            it.loadMoreComplete()
            it.stopShimmer()
        }
    }

    fun clearRecyclerView(recyclerView: BaseRecyclerView?) {
        recyclerView?.destroy()
    }

}