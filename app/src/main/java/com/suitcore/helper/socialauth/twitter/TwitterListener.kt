package com.suitcore.helper.socialauth.twitter

/**
 * Created by dodydmw19 on 7/16/18.
 */

interface TwitterListener {

    fun onTwitterError(errorMessage: String?)

    fun onTwitterSignIn(authToken: String?, secret: String?, userId: String?)

}