package com.suitcore.helper.socialauth.twitter

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.annotation.StringRes

import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import com.twitter.sdk.android.core.models.User

/**
 * Created by dodydmw19 on 12/14/18.
 */

class TwitterHelper(@StringRes twitterApiKey: Int,
                    @StringRes twitterSecreteKey: Int,
                    private val mListener: TwitterListener?,
                    private val mActivity: Activity?) {
    private val mAuthClient: TwitterAuthClient?

    init {

        if (mListener == null) throw IllegalArgumentException("TwitterResponse cannot be null.")

        //initialize sdk
        val authConfig = TwitterConfig.Builder(mActivity)
                .logger(DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(TwitterAuthConfig(mActivity?.resources?.getString(twitterApiKey),
                        mActivity?.resources?.getString(twitterSecreteKey)))
                .debug(true)
                .build()
        Twitter.initialize(authConfig)

        mAuthClient = TwitterAuthClient()
    }

    /**
     * Perform twitter sign in. Call this method when user clicks on "Login with Twitter" button.
     */
    fun performSignIn() {
        mAuthClient?.authorize(mActivity, object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>) {
                val session = result.data

                //load user data.
                getUserData(session)
            }

            override fun failure(exception: TwitterException) {

                mListener?.onTwitterError("Twitter Authentication Failed")

            }
        })
    }

    /**
     * This method handles onActivityResult callbacks from fragment or activity.
     *
     * @param requestCode request code received.
     * @param resultCode  result code received.
     * @param data        Data intent.
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        mAuthClient?.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Load twitter user profile.
     */
    private fun getUserData(session: TwitterSession) {
        val twitterApiClient = TwitterCore.getInstance().apiClient
        val statusesService = twitterApiClient.accountService
        val call = statusesService.verifyCredentials(true, true, true)
        call.enqueue(object : Callback<User>() {
            override fun success(userResult: Result<User>) {
                //Do something with result

                //parse the response
                val user = TwitterUser()

                user.name = userResult.data.name
                user.email = userResult.data.email
                user.description = userResult.data.description
                user.pictureUrl = userResult.data.profileImageUrl
                user.bannerUrl = userResult.data.profileBannerUrl
                user.language = userResult.data.lang
                user.id = userResult.data.id

                val authToken = session.authToken
                val token = authToken.token
                val secret = authToken.secret
                mListener?.onTwitterSignIn(token, secret, userResult.data.id.toString())
            }

            override fun failure(exception: TwitterException) {
                //Do something on failure
                mListener?.onTwitterError("Twitter Authentication Failed")
            }
        })
    }
}

