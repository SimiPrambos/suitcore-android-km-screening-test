package com.suitcore.helper

import android.app.Activity
import android.content.Intent
import com.suitcore.feature.splashscreen.SplashScreenActivity
import kotlin.system.exitProcess

/**
 * Created by dodydmw19 on 2/18/19.
 */

class DefaultExceptionHandler(private val activity: Activity?) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        activity?.let {
            // Re-Launch Application and give user information
            val intent = Intent(activity, SplashScreenActivity::class.java)
            intent.putExtra(CommonConstant.APP_CRASH, "app crash")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            it.startActivity(intent)
            it.finish()
            exitProcess(0)
        }
    }

}
