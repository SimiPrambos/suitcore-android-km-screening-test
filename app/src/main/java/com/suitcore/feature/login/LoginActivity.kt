package com.suitcore.feature.login

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.viewbinding.ViewBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.suitcore.R
import com.suitcore.base.ui.BaseActivity
import com.suitcore.databinding.ActivityLoginBinding
import com.suitcore.feature.sidemenu.SideMenuActivity
import com.suitcore.feature.tabmenu.TabMenuActivity
import com.suitcore.firebase.analytics.FireBaseConstant
import com.suitcore.firebase.analytics.FireBaseHelper
import com.suitcore.firebase.remoteconfig.RemoteConfigHelper
import com.suitcore.firebase.remoteconfig.RemoteConfigPresenter
import com.suitcore.firebase.remoteconfig.RemoteConfigView
import com.suitcore.helper.CommonConstant
import com.suitcore.helper.CommonUtils
import com.suitcore.helper.permission.SuitPermissions
import com.suitcore.helper.socialauth.facebook.FacebookHelper
import com.suitcore.helper.socialauth.facebook.FacebookListener
import com.suitcore.helper.socialauth.google.GoogleListener
import com.suitcore.helper.socialauth.google.GoogleSignInHelper
import com.suitcore.helper.socialauth.twitter.TwitterHelper
import com.suitcore.helper.socialauth.twitter.TwitterListener

/**
 * Created by dodydmw19 on 7/18/18.
 */

class LoginActivity : BaseActivity(), LoginView, RemoteConfigView, GoogleListener, FacebookListener, TwitterListener {

    private var loginPresenter: LoginPresenter? = null
    private var remoteConfigPresenter: RemoteConfigPresenter? = null

    private var mGoogleHelper: GoogleSignInHelper? = null
    private var mTwitterHelper: TwitterHelper? = null
    private var mFbHelper: FacebookHelper? = null
    private lateinit var loginBinding : ActivityLoginBinding

    override fun setBinding(layoutInflater: LayoutInflater) = initBinding(layoutInflater)

    private fun initBinding(layoutInflater: LayoutInflater) : ViewBinding {
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        return loginBinding
    }

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupPresenter()
        setupSocialLogin()
        actionClicked()
        needPermissions()
    }

    override fun onResume() {
        super.onResume()
        sendAnalytics()
        remoteConfigPresenter?.checkUpdate(CommonConstant.CHECK_APP_VERSION) // check app version and notify update from remote config
        remoteConfigPresenter?.checkUpdate(CommonConstant.CHECK_BASE_URL) // check base url from remote config if any changes
    }

    private fun sendAnalytics(){
        FireBaseHelper.instance().getFireBaseAnalytics()?.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW){
            param(FirebaseAnalytics.Param.SCREEN_NAME, FireBaseConstant.SCREEN_LOGIN)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, LoginActivity::class.java.simpleName)
        }
    }

    private fun setupPresenter() {
        loginPresenter = LoginPresenter()
        loginPresenter?.attachView(this)
    }

    private fun needPermissions() {
        SuitPermissions.with(this)
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE)
                .onAccepted {
                    for (s in it) {
                        Log.d("granted_permission", s)
                    }
                    showToast("Granted")
                }
                .onDenied {
                    showToast("Denied")
                }
                .onForeverDenied {
                    showToast("Forever denied")
                }
                .ask()
    }

    private fun setupSocialLogin() {
        // Google  initialization
        mGoogleHelper = GoogleSignInHelper(this, R.string.google_default_web_client_id, this)

        // twitter initialization
        mTwitterHelper = TwitterHelper(
                R.string.twitter_api_key,
                R.string.twitter_secret_key,
                this,
                this)

        // fb initialization
        mFbHelper = FacebookHelper(this, getString(R.string.facebook_request_field))

        signOut()
    }

    private fun signOut() {
        mGoogleHelper?.performSignOut()
        mFbHelper?.performSignOut()
    }

    override fun onLoginSuccess(message: String?) {
        //goToActivity(MemberActivity::class.java, null, clearIntent = true, isFinish = true)
        showToast("Login Success")
    }

    override fun onLoginFailed(message: String?) {
        message?.let {
            showToast(message.toString())
        }
    }

    override fun onGoogleAuthSignIn(authToken: String?, userId: String?) {
        // send token & user_id to server
        loginPresenter?.login()
    }

    override fun onGoogleAuthSignInFailed(errorMessage: String?) {
        showToast(errorMessage.toString())
    }

    override fun onFbSignInFail(errorMessage: String?) {
        showToast(errorMessage.toString())
    }

    override fun onFbSignInSuccess(authToken: String?, userId: String?) {
        // send token & user_id to server
        loginPresenter?.login()
    }

    override fun onTwitterError(errorMessage: String?) {
        showToast(errorMessage.toString())
    }

    override fun onTwitterSignIn(authToken: String?, secret: String?, userId: String?) {
        // send token & user_id to server
        loginPresenter?.login()
    }

    override fun onUpdateAppNeeded(forceUpdate: Boolean, message: String?) {
        when (forceUpdate) {
            true -> {
                val confirmDialog = AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage(message)
                        .setPositiveButton("OK") { d, _ ->
                            d.dismiss()
                            CommonUtils.openAppInStore(this)
                        }
                        .create()

                confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                confirmDialog.show()
            }
            false -> {
                val confirmDialog = AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage(message)
                        .setPositiveButton("OK") { d, _ ->
                            d.dismiss()
                            CommonUtils.openAppInStore(this)
                        }
                        .setNegativeButton("CANCEL") { d, _ -> d.dismiss() }
                        .create()

                confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                confirmDialog.show()
            }
        }
    }

    override fun onUpdateBaseUrlNeeded(type: String?, url: String?) {
        RemoteConfigHelper.changeBaseUrl(this, type.toString(), url.toString())
    }

    private fun actionClicked() {
        loginBinding.relGoogle.setOnClickListener {
            mGoogleHelper?.performSignIn(this)
        }

        loginBinding.relFacebook.setOnClickListener {
            mFbHelper?.performSignIn(this)
        }

        loginBinding.relTwitter.setOnClickListener {
            if (CommonUtils.checkTwitterApp(this)) {
                mTwitterHelper?.performSignIn()
            } else {
                showToast(getString(R.string.txt_twitter_not_installed))
            }
        }

        loginBinding.tvSkipToTabMenu.setOnClickListener {
            goToActivity(TabMenuActivity::class.java, null, clearIntent = true, isFinish = true)
        }

        loginBinding.tvSkipToSideMenu.setOnClickListener {
            goToActivity(SideMenuActivity::class.java, null, clearIntent = true, isFinish = true)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            mGoogleHelper?.onActivityResult(requestCode, resultCode, data)
            mTwitterHelper?.onActivityResult(requestCode, resultCode, data)
            mFbHelper?.onActivityResult(requestCode, resultCode, data)
        }
    }

}