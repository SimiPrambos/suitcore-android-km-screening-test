package com.suitcore.helper.localization

import android.content.res.Configuration

import java.util.Locale
import android.os.Build
import android.annotation.TargetApi
import android.content.Context


@Suppress("DEPRECATION")
/**
 * Created by DODYDMW19 on 12/20/2017.
 */

object LanguageHelper {

    @Suppress("DEPRECATION")
    fun getSystemLocaleLegacy(config: Configuration): Locale {
        return config.locale
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun getSystemLocale(config: Configuration): Locale {
        return config.locales.get(0)
    }

    @Suppress("DEPRECATION")
    private fun setSystemLocaleLegacy(config: Configuration, locale: Locale) {
        config.locale = locale
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun setSystemLocale(config: Configuration, locale: Locale) {
        config.setLocale(locale)
    }

    fun setLanguage(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setSystemLocale(config, locale)
        } else {
            setSystemLocaleLegacy(config, locale)
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
            context.applicationContext.resources.updateConfiguration(config,
                    context.resources.displayMetrics)
    }
}
