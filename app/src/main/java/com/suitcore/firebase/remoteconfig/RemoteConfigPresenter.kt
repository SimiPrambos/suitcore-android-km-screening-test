package com.suitcore.firebase.remoteconfig

import androidx.lifecycle.LifecycleOwner
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.suitcore.BaseApplication
import com.suitcore.BuildConfig
import com.suitcore.R
import com.suitcore.base.presenter.BasePresenter
import com.suitcore.data.local.prefs.DataConstant
import com.suitcore.data.local.prefs.SuitPreferences
import com.suitcore.helper.CommonConstant
import com.suitcore.helper.CommonUtils

/**
 *
 *  Field Params :
 *  force_message -> for message content force update
 *  info_message -> for message content info update (can deny)
 *  minumum_force_android -> latest versionCode for force update
 *  minimum_info_android -> latest versionCode for info update
 *
 */


class RemoteConfigPresenter : BasePresenter<RemoteConfigView> {

    private var mFireBaseRemoteConfig: FirebaseRemoteConfig? = null
    private var mvpView: RemoteConfigView? = null
    private val cacheExpiration: Long = 0 // 1 hour in seconds.

    init {
        BaseApplication.applicationComponent.inject(this)
        setupFireBaseRemoteConfig()
    }

    private fun setupFireBaseRemoteConfig() {
        mFireBaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        mFireBaseRemoteConfig?.setConfigSettingsAsync(
                FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(3600L)
                        .build())

        mFireBaseRemoteConfig?.setDefaultsAsync(R.xml.remote_config_defaults)
    }

    fun checkUpdate(type: String) {
        if (mFireBaseRemoteConfig == null) {
            setupFireBaseRemoteConfig()
        }

        mFireBaseRemoteConfig?.fetch(cacheExpiration)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // After config data is successfully fetched, it must be activated before newly fetched
                        // values are returned.
                        mFireBaseRemoteConfig?.activate()
                    }
                    when (type) {
                        CommonConstant.CHECK_APP_VERSION -> checkVersion()
                        CommonConstant.CHECK_BASE_URL -> {
                            val newBaseUrl: String? = mFireBaseRemoteConfig?.getString(CommonConstant.NEW_BASE_URL)
                            val currentUrl: String? = SuitPreferences.instance()?.getString(DataConstant.BASE_URL)
                            if (newBaseUrl != null) {
                                if (newBaseUrl.toString().isNotEmpty()) {
                                    if (currentUrl != newBaseUrl) mvpView?.onUpdateBaseUrlNeeded("new", newBaseUrl.toString())
                                } else {
                                    if (currentUrl != null && currentUrl.isNotEmpty() && currentUrl != BuildConfig.BASE_URL) {
                                        mvpView?.onUpdateBaseUrlNeeded("default", BuildConfig.BASE_URL)
                                    } else {
                                        CommonUtils.setDefaultBaseUrlIfNeeded()
                                    }
                                }
                            }
                        }
                    }
                }
    }

    private fun checkVersion() {
        val currentVersion = java.lang.Double.valueOf(BuildConfig.VERSION_CODE.toDouble())
        var normalUpdateVersion: Double? = 0.0
        var forceUpdateVersion: Double? = 0.0

        if (mFireBaseRemoteConfig?.getString(CommonConstant.NOTIFY_FORCE_UPDATE) != null && mFireBaseRemoteConfig?.getString(CommonConstant.NOTIFY_FORCE_UPDATE)!!.isNotEmpty()) {
            val info = mFireBaseRemoteConfig?.getString(CommonConstant.NOTIFY_FORCE_UPDATE)
            forceUpdateVersion = if (info != null) {
                java.lang.Double.parseDouble(info)
            } else {
                0.0
            }
        }

        if (mFireBaseRemoteConfig?.getString(CommonConstant.NOTIFY_NORMAL_UPDATE) != null && mFireBaseRemoteConfig?.getString(CommonConstant.NOTIFY_NORMAL_UPDATE)!!.isNotEmpty()) {
            normalUpdateVersion = try {
                val info = mFireBaseRemoteConfig?.getString(CommonConstant.NOTIFY_NORMAL_UPDATE)
                if (info != null) {
                    java.lang.Double.parseDouble(info)
                } else {
                    0.0
                }
            } catch (e: Exception) {
                e.printStackTrace()
                0.0
            }
        }

        val messages: String

        if (forceUpdateVersion != 0.0 && currentVersion < forceUpdateVersion!!) {
            try {
                messages = mFireBaseRemoteConfig?.getString(CommonConstant.NOTIFY_FORCE_MESSAGE).toString()
                mvpView?.onUpdateAppNeeded(true, messages)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return
        }

        if (normalUpdateVersion != 0.0 && currentVersion < normalUpdateVersion!!) {
            try {
                messages = mFireBaseRemoteConfig?.getString(CommonConstant.NOTIFY_NORMAL_MESSAGE).toString()
                mvpView?.onUpdateAppNeeded(false, messages)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return
        }
    }

    override fun onDestroy() {
        detachView()
    }

    override fun attachView(view: RemoteConfigView) {
        mvpView = view
        // Initialize this presenter as a lifecycle-aware when a view is a lifecycle owner.
        if (mvpView is LifecycleOwner) {
            (mvpView as LifecycleOwner).lifecycle.addObserver(this)
        }
    }

    override fun detachView() {
        mvpView = null
    }

}