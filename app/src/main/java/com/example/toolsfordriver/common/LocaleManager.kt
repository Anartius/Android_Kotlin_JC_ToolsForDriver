package com.example.toolsfordriver.common

import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
import com.example.toolsfordriver.data.Locales
import java.util.Locale

object LocaleManager {
    fun setLocale(context: Context, locale: Locale): Context {
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLocales(LocaleList(locale))

        return context.createConfigurationContext(config)
    }

    fun saveLocale(context: Context, locale: Locale) {
        val sharedPreferences = context.getSharedPreferences("LocalePrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("language", locale.language).apply()
    }

    fun getSavedLocale(context: Context): Locale {
        val sharedPreferences = context.getSharedPreferences("LocalePrefs", Context.MODE_PRIVATE)

        if (!sharedPreferences.contains("language")) {
            val defaultLocale = Locale.getDefault()
            var isLocaleInSupportList = false

            for (locale in Locales.entries) {
                if (locale.locale.language == defaultLocale.language) isLocaleInSupportList = true
            }

            saveLocale(
                context,
                if (isLocaleInSupportList) {
                    Locale.getDefault()
                } else Locales.LOCALE_EN.locale
            )
        }

        return try {
            Locale(sharedPreferences.getString("language", Locales.LOCALE_EN.locale.language)!!)
        } catch (e: Exception) {
            Locales.LOCALE_EN.locale
        }
    }
}