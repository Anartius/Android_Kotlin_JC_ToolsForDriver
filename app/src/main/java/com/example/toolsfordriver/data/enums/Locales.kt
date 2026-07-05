package com.example.toolsfordriver.data.enums

import java.util.Locale

enum class Locales(val locale: Locale) {
    LOCALE_EN(Locale.forLanguageTag("en")),
    LOCALE_RU(Locale.forLanguageTag("ru"))
}