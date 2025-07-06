package com.example.toolsfordriver.common

import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun dateAsString(dateTime: Long?): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.ROOT)
    return if (dateTime != null) {
        formatter.format(Date(dateTime))
    } else ""
}

fun dateAsString(dateTime: Date?): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.ROOT)
    return if (dateTime != null) {
        formatter.format(dateTime)
    } else ""
}

fun dateAsString(dateTime: LocalDateTime?): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ROOT)
    return if (dateTime != null) {
        formatter.format(dateTime)
    } else ""
}

fun dateAsString(dateTime: ZonedDateTime?): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ROOT)
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

fun durationAsString(duration: Duration): String {
    val days = duration.toDays()
    val hours = duration.toHours() % 24

    return ((if (days != 0L) "$days d " else "") + "$hours h")
}
