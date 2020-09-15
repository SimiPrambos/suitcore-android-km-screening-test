package com.suitcore.helper.socialauth.google

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by dodydmw19 on 12/14/18.
 */

class GoogleSignInHelper(private val mContext: FragmentActivity?,
                         serverClientId: Int,
                         private val mListener: GoogleListener?) : GoogleApiClient.OnConnectionFailedListener {

    private var mGoogleApiClient: GoogleApiClient? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var mAuth: FirebaseAuth? = null

    init {
        if (mListener == null) {
            throw RuntimeException("GoogleAuthResponse listener cannot be null.")
        }

        //build api client
        buildGoogleApiClient(buildSignInOptions(mContext?.getString(serverClientId)))

        mAuth = FirebaseAuth.getInstance()
    }

    private fun buildSignInOptions(serverClientId: String?): GoogleSignInOptions {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
        if (serverClientId != null) gso.requestIdToken(serverClientId)
        if (mContext != null) {
            mGoogleSignInClient = GoogleSignIn.getClient(mContext, gso.build())
        }
        return gso.build()
    }

    private fun buildGoogleApiClient(gso: GoogleSignInOptions) {
        if (mContext != null) {
            mGoogleApiClient = GoogleApiClient.Builder(mContext)
                    .enableAutoManage(mContext, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build()
        }
    }

    fun performSignIn(activity: Activity) {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun performSignIn(activity: Fragment) {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                mListener?.onGoogleAuthSignIn(parseToGoogleUser(account!!).idToken, parseToGoogleUser(account).id)
                //asyncGoogle(account)
            } catch (e: ApiException) {
                mListener?.onGoogleAuthSignInFailed("Google Authentication Failed")
            }
        }
    }

    private fun asyncGoogle(account: GoogleSignInAccount?) {
        if (mContext != null && account != null) {
            Observable.create(ObservableOnSubscribe<String> { emitter ->
                try {
                    val scope = "oauth2:" + Scopes.EMAIL + " " + Scopes.PROFILE
                    val token = GoogleAuthUtil.getToken(mContext, account.account, scope, Bundle())
                    //send token to server
                    mListener?.onGoogleAuthSignIn(token.toString(), account.id)
                } catch (e: Exception) {
                    emitter.onError(e) // In case there are network errors
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
        }
    }

    private fun parseToGoogleUser(account: GoogleSignInAccount): GoogleAuthUser {
        val user = GoogleAuthUser()
        user.name = account.displayName
        user.familyName = account.familyName
        user.idToken = account.idToken
        user.email = account.email
        user.photoUrl = account.photoUrl
        return user
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        mListener?.onGoogleAuthSignInFailed("Google Authentication Failed")
    }

    fun performSignOut() {
        if (mAuth != null && mContext != null) {
            mAuth?.signOut()
            mGoogleSignInClient?.signOut()?.addOnCompleteListener(mContext) { }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 100
    }
}
