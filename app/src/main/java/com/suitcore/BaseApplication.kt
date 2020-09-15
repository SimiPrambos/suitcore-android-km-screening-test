package com.suitcore

import android.app.Activity
import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.facebook.drawee.backends.pipeline.Fresco
import com.mapbox.mapboxsdk.Mapbox
import com.onesignal.OneSignal
import com.suitcore.data.local.prefs.DataConstant
import com.suitcore.data.local.prefs.SuitPreferences
import com.suitcore.di.component.ApplicationComponent
import com.suitcore.di.component.DaggerApplicationComponent
import com.suitcore.di.module.ApplicationModule
import com.suitcore.helper.ActivityLifecycleCallbacks
import com.suitcore.helper.CommonConstant
import com.suitcore.helper.CommonUtils
import com.suitcore.helper.rxbus.RxBus
import com.suitcore.onesignal.NotificationOpenedHandler
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by DODYDMW19 on 1/30/2018.
 */

class BaseApplication : MultiDexApplication() {

    val mActivityLifecycleCallbacks = ActivityLifecycleCallbacks()

    init {
        instance = this
    }

    companion object {
        lateinit var applicationComponent: ApplicationComponent
        lateinit var appContext: Context
        lateinit var bus: RxBus
        private var instance: BaseApplication? = null

        fun currentActivity(): Activity? {
            return instance?.mActivityLifecycleCallbacks?.currentActivity
        }
    }

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
        //bus = RxBus()

        // Initial Preferences
        SuitPreferences.init(applicationContext)

        CommonUtils.setDefaultBaseUrlIfNeeded()

        appContext = applicationContext
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()

        Fresco.initialize(this)

        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                //.encryptionKey(CommonUtils.getKey()) // encrypt realm
                .build()
        Realm.setDefaultConfiguration(realmConfiguration)

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(NotificationOpenedHandler())
                .init()
        OneSignal.enableVibrate(true)
        OneSignal.enableSound(true)
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        OneSignal.idsAvailable { userId, _ ->

            if (userId != null) {
                SuitPreferences.instance()?.saveString(DataConstant.PLAYER_ID, userId)
            }
        }

        Mapbox.getInstance(this, CommonConstant.MAP_BOX_TOKEN)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}