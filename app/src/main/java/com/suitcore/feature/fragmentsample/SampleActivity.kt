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
import com.suitcore.data.model.UpdateType
import com.suitcore.databinding.ActivityTestBinding
import com.suitcore.firebase.remoteconfig.RemoteConfigPresenter
import com.suitcore.firebase.remoteconfig.RemoteConfigView
import com.suitcore.helper.CommonConstant.UpdateMode
import com.suitcore.helper.inappupdates.InAppUpdateManager
import com.suitcore.helper.inappupdates.InAppUpdateStatus
import timber.log.Timber

/**
 * Created by dodydmw19 on 7/30/18.
 */

class SampleActivity : BaseActivity(){

    private lateinit var mCurrentFragment: Fragment
    private lateinit var sampleActivityBinding: ActivityTestBinding

    override fun setBinding(layoutInflater: LayoutInflater) = initBinding(layoutInflater)

    private fun initBinding(layoutInflater: LayoutInflater) : ViewBinding {
        sampleActivityBinding = ActivityTestBinding.inflate(layoutInflater)
        return sampleActivityBinding
    }

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupToolbar(sampleActivityBinding.mToolbar, true)
        setContentFragment(SampleFragment.newInstance())
    }

    private fun setContentFragment(fragment: Fragment) {
        mCurrentFragment = fragment
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, mCurrentFragment)
                .commitAllowingStateLoss()
    }


}