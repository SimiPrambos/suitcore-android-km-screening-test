package com.suitcore.feature.splashscreen

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.suitcore.R
import com.suitcore.base.ui.BaseActivity
import com.suitcore.databinding.ActivitySplashscreenBinding
import com.suitcore.feature.login.LoginActivity
import com.suitcore.helper.CommonConstant

/**
 * Created by dodydmw19 on 12/19/18.
 */

class SplashScreenActivity : BaseActivity(), SplashScreenView {

    private var splashScreenPresenter: SplashScreenPresenter? = null
    private lateinit var splashScreenBinding: ActivitySplashscreenBinding

    override fun setBinding(layoutInflater: LayoutInflater) = initBinding(layoutInflater)

    private fun initBinding(layoutInflater: LayoutInflater): ViewBinding {
        splashScreenBinding = ActivitySplashscreenBinding.inflate(layoutInflater)
        return splashScreenBinding
    }

    override fun onViewReady(savedInstanceState: Bundle?) {
        changeProgressBarColor(R.color.white, splashScreenBinding.progressBar)
        setupPresenter()
    }

    private fun setupPresenter() {
        splashScreenPresenter = SplashScreenPresenter()
        splashScreenPresenter?.attachView(this)
        splashScreenPresenter?.initialize()
    }

    override fun navigateToMainView() {
        goToActivity(LoginActivity::class.java,  null, clearIntent = true, isFinish = true)
    }

}