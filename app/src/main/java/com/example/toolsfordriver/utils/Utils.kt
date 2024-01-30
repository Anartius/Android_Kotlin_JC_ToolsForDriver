package com.example.toolsfordriver.utils

import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import kotlinx.datetime.toInstant
import kotlinx.datetime.until
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.hours

fun dateAsString(dateTime: Long?): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
    return if (dateTime != null) {
        formatter.format(Date(dateTime))
    } else ""
}

fun timeAsString(dateTime: Long?): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.ROOT)
    return if (dateTime != null) {
        formatter.format(Date(dateTime))
    } else ""
}

fun calcPeriod(startDateTime: LocalDateTime?, endDateTime: LocalDateTime?): DateTimePeriod? {
    return if (startDateTime != null && endDateTime != null) {
        val start = startDateTime.toInstant(TimeZone.currentSystemDefault())
        val end = endDateTime.toInstant(TimeZone.currentSystemDefault())
        start.periodUntil(end, TimeZone.currentSystemDefault())
    } else null
}

fun calcEarnings(
    startDateTime: LocalDateTime?,
    endDateTime: LocalDateTime?,
    moneyPerHour: Double
): Double? {
    return if (startDateTime != null && endDateTime != null) {
        val start = startDateTime.toInstant(TimeZone.currentSystemDefault())
        val end = endDateTime.toInstant(TimeZone.currentSystemDefault())
        val duration = start.until(end, DateTimeUnit.HOUR).hours.inWholeHours
        ((moneyPerHour * duration) * 100).roundToInt() / 100.0
    } else null
}

fun String.isValidEmail(): Boolean {
    val emailAddressPattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
    return emailAddressPattern.matcher(this).matches()
}