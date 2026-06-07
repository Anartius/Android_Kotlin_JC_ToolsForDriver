package com.example.toolsfordriver.common

import android.content.Context
import com.example.toolsfordriver.R
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun dateAsString(dateTime: Long?, pattern: String = "dd.MM.yyyy"): String {
    val formatter = SimpleDateFormat(pattern, Locale.ROOT)
    return if (dateTime != null) {
        formatter.format(Date(dateTime))
    } else ""
}

fun dateAsString(dateTime: Date?, pattern: String = "dd.MM.yyyy"): String {
    val formatter = SimpleDateFormat(pattern, Locale.ROOT)
    return if (dateTime != null) {
        formatter.format(dateTime)
    } else ""
}

fun dateAsString(dateTime: LocalDateTime?, pattern: String = "dd.MM.yyyy"): String {
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.ROOT)
    return if (dateTime != null) {
        formatter.format(dateTime)
    } else ""
}

fun dateAsString(dateTime: ZonedDateTime?, pattern: String = "dd.MM.yyyy"): String {
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.ROOT)
    return if (dateTime != null) {
        formatter.format(dateTime)
    } else ""
}

fun timeAsString(dateTime: Date?): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.ROOT)
    return if (dateTime != null) {
        formatter.format(dateTime)
    } else ""
}

fun timeAsString(dateTime: LocalDateTime?): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ROOT)
    return if (dateTime != null) {
        formatter.format(dateTime)
    } else ""
}

fun timeAsString(dateTime: ZonedDateTime?): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ROOT)
    return if (dateTime != null) {
        formatter.format(dateTime)
    } else ""
}

fun durationAsString(duration: Duration?, context: Context): String {
    val days = duration?.toDays() ?: 0L
    val hours = if (duration == null || duration.isZero) 0L else duration.toHours() % 24

    return (if (days != 0L) "$days ${context.getString(R.string.d)} " else "") +
            "$hours ${context.getString(R.string.h)}"
}
