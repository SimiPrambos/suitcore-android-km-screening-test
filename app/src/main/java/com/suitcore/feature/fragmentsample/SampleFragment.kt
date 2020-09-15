package com.suitcore.feature.fragmentsample

import android.os.Bundle
import com.suitcore.R
import com.suitcore.base.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_test.*


/**
 * Created by dodydmw19 on 7/30/18.
 */

class SampleFragment : BaseFragment() {

    companion object {
        fun newInstance(): BaseFragment {
            return SampleFragment()
        }
    }

    private val actionClicked = ::dialogPositiveAction

    override val resourceLayout: Int
        get() = R.layout.fragment_test

    override fun onViewReady(savedInstanceState: Bundle?) {
        actionClicked()
    }

    private fun dialogPositiveAction() {
        showToast("click")
    }

    private fun actionClicked() {
        relAlertDialog.setOnClickListener {
            showAlertDialog("test")
        }

        relCondirmDialog.setOnClickListener {
            showConfirmationDialog("test", actionClicked)
        }
    }

}