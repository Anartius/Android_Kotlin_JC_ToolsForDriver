package com.example.toolsfordriver.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import kotlinx.datetime.toInstant
import kotlinx.datetime.until
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.hours

fun dateAsString(dateTime: Long?): String {
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.ROOT)
    return if (dateTime != null) {
        formatter.format(Date(dateTime))
    } else ""
}

fun dateAsStringIso(dateTime: Long?): String {
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

fun formatPeriod(period: DateTimePeriod): String {
    val years = period.years
    val months = period.months
    val days = period.days
    val hours = period.hours
    val minutes = period.minutes

    return (if (years != 0) "$years y " else "") +
            (if (months != 0) "$months m " else "") +
            (if (days != 0) "$days d " else "") +
            "$hours h" +
            (if (minutes != 0) " $minutes min" else "")
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

fun deleteImageFromInternalStorage(
    uri: Uri,
    context: Context
): Boolean {
    return try {
        val directory = context.filesDir
        val fileName = uri.pathSegments.last()
        val file = File(directory, fileName)
        file.delete()
        true
    } catch (e: Exception) {
        Log.e("Image delete", e.message.toString())
        Toast.makeText(context, "Image wasn't delete", Toast.LENGTH_LONG).show()
        false
    }
}