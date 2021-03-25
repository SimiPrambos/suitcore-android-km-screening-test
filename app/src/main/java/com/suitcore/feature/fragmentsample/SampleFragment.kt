package com.suitcore.feature.fragmentsample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.suitcore.R
import com.suitcore.base.ui.BaseFragment
import com.suitcore.databinding.FragmentTestBinding

/**
 * Created by dodydmw19 on 7/30/18.
 */

class SampleFragment : BaseFragment() {

    companion object {
        fun newInstance(): BaseFragment {
            return SampleFragment()
        }
    }

    private lateinit var sampleBinding: FragmentTestBinding
    private val actionClicked = ::dialogPositiveAction

    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): ViewBinding {
        sampleBinding = FragmentTestBinding.inflate(inflater, container, false)
        return sampleBinding
    }

    override fun onViewReady(savedInstanceState: Bundle?) {
        actionClicked()
    }

    private fun dialogPositiveAction() {
        showToast("click")
    }

    private fun actionClicked() {
        sampleBinding.relAlertDialog.setOnClickListener {
            showAlertDialog("old Alert")
        }

        sampleBinding.relCondirmDialog.setOnClickListener {
            showConfirmationDialog("old Confirmation", actionClicked)
        }

        sampleBinding.relNewAlertDialog.setOnClickListener {
            showDialogAlert(title = null,message = "New Alert")
        }

        sampleBinding.relNewImageAlertDialog.setOnClickListener {
            showDialogAlert(title = null, message ="New Alert Image",drawableImage = R.drawable.ic_logo_suitcore_main)
        }

        sampleBinding.relNewConfirmDialog.setOnClickListener {
            showDialogConfirmation(title = null, message ="New Confirmation Without Image", confirmCallback = {
                showDialogLoading(true,null)
            })
        }

        sampleBinding.relNewConfirmImageDialog.setOnClickListener {
            showDialogConfirmation(title = null, message = "New Confirmation With Image", drawableImage = R.drawable.ic_logo_suitcore_main,confirmCallback = {
                showDialogLoading(true,"Press back to dismiss")
            })
        }

        sampleBinding.relNewContentDialog.setOnClickListener {
            showDialogPopImage(R.drawable.ic_logo_suitcore_main)
        }
    }

}