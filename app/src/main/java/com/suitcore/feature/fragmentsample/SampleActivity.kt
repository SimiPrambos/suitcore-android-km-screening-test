package com.suitcore.feature.fragmentsample

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.suitcore.R
import com.suitcore.base.ui.BaseActivity
import com.suitcore.databinding.ActivityTestBinding
import com.suitcore.helper.CommonConstant.UpdateMode
import com.suitcore.helper.inappupdates.InAppUpdateManager
import com.suitcore.helper.inappupdates.InAppUpdateStatus
import timber.log.Timber


/**
 * Created by dodydmw19 on 7/30/18.
 */

class SampleActivity : BaseActivity(), InAppUpdateManager.InAppUpdateHandler {

    private lateinit var mCurrentFragment: Fragment
    private lateinit var sampleActivityBinding: ActivityTestBinding
    private var inAppUpdateManager: InAppUpdateManager? = null

    override fun setBinding(layoutInflater: LayoutInflater) = initBinding(layoutInflater)

    private fun initBinding(layoutInflater: LayoutInflater) : ViewBinding {
        sampleActivityBinding = ActivityTestBinding.inflate(layoutInflater)
        return sampleActivityBinding
    }

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupToolbar(sampleActivityBinding.mToolbar, true)
        setContentFragment(SampleFragment.newInstance())
        setupInAppUpdate()
    }

    private fun setContentFragment(fragment: Fragment) {
        mCurrentFragment = fragment
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, mCurrentFragment)
                .commitAllowingStateLoss()
    }

    private fun setupInAppUpdate(){
        inAppUpdateManager = InAppUpdateManager.builder(this, 101)
                ?.resumeUpdates(true) // Resume the update, if the update was stalled. Default is true
                ?.mode(UpdateMode.FLEXIBLE)
                ?.snackBarMessage("An update has just been downloaded.")
                ?.snackBarAction("RESTART")
                ?.handler(this)

        inAppUpdateManager?.checkForAppUpdate()
    }

    override fun onInAppUpdateError(code: Int, error: Throwable?) {
        Timber.d(error, error?.message.toString())
    }

    override fun onInAppUpdateStatus(status: InAppUpdateStatus?) {
        if (status?.isDownloaded == true) {
            val rootView: View = window.decorView.findViewById(android.R.id.content)
            val snackBar = Snackbar.make(rootView,
                    "An update has just been downloaded.",
                    Snackbar.LENGTH_INDEFINITE)
            snackBar.setAction("RESTART") {

                // Triggers the completion of the update of the app for the flexible flow.
                inAppUpdateManager?.completeUpdate()
            }
            snackBar.show()
        }
    }


    @SuppressLint("TimberArgCount")
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        if (requestCode == 101) {
            if (resultCode == RESULT_CANCELED) {
                // If the update is cancelled by the user,
                // you can request to start the update again.
                inAppUpdateManager!!.checkForAppUpdate()
                Timber.d("inappupdate", "Update flow failed! Result code: $resultCode")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}