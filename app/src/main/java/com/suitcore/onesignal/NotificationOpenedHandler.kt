package com.suitcore.onesignal

import android.content.Intent
import com.onesignal.OSNotificationOpenResult
import com.onesignal.OneSignal
import com.suitcore.BaseApplication
import com.suitcore.feature.login.LoginActivity

class NotificationOpenedHandler : OneSignal.NotificationOpenedHandler {

    override fun notificationOpened(result: OSNotificationOpenResult) {
        //val data = result.notification.payload.additionalData
        val intent = Intent(BaseApplication.appContext, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        BaseApplication.appContext.startActivity(intent)
    }
}