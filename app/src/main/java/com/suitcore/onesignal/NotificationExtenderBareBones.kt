package com.suitcore.onesignal

import com.onesignal.NotificationExtenderService
import com.onesignal.OSNotificationReceivedResult

class NotificationExtenderBareBones: NotificationExtenderService() {
    override fun onNotificationProcessing(notification: OSNotificationReceivedResult?): Boolean {
        // Read properties from result.

        // Return true to stop the notification from displaying.
        return false
    }
}