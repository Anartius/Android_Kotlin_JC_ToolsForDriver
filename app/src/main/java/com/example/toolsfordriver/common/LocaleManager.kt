package com.example.toolsfordriver.common

import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
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
        val language = sharedPreferences.getString("language", Locale.getDefault().language)
        return Locale(language?: Locale.getDefault().language)
    }
}