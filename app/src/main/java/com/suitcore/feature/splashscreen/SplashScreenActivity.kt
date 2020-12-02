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
    private val actionClicked = ::dialogPositiveAction
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

    private fun handleIntent(){
        val data: Bundle? = intent.extras
        if(data?.getString(CommonConstant.APP_CRASH) != null){
            showConfirmationSingleDialog(getString(R.string.txt_error_crash), actionClicked)
        }else{
            splashScreenPresenter?.initialize()
        }
    }

    private fun setupPresenter() {
        splashScreenPresenter = SplashScreenPresenter()
        splashScreenPresenter?.attachView(this)
        handleIntent()
    }

    override fun navigateToMainView() {
        goToActivity(LoginActivity::class.java,  null, clearIntent = true, isFinish = true)
    }

    private fun dialogPositiveAction() {
        splashScreenPresenter?.initialize()
    }

}