package com.example.toolsfordriver.common

import java.time.Duration
import java.time.LocalDateTime

fun calcDuration(
    startDateTime: LocalDateTime?,
    endDateTime: LocalDateTime?,
    roundUpFromMinutes: Int
): Duration? {
    return if (startDateTime != null && endDateTime != null) {
        val start = roundFromMinutes(startDateTime, roundUpFromMinutes)
        val end = roundFromMinutes(endDateTime, roundUpFromMinutes)

        Duration.between(start, end)
    } else null
}

fun calcEarnings(
    startDateTime: LocalDateTime?,
    endDateTime: LocalDateTime?,
    roundUpFromMinutes: Int,
    moneyPerHour: Double
): Double? {
    return if (startDateTime != null && endDateTime != null) {
        val duration = calcDuration(startDateTime, endDateTime, roundUpFromMinutes)!!.toHours()

        moneyPerHour * duration
    } else null
}


fun roundFromMinutes(value: LocalDateTime, roundFrom: Int): LocalDateTime {
    val minute = value.minute.toLong()
    val amountOfMinutes = if (minute >= roundFrom) 60 - minute  else -minute

    return value.plusMinutes(amountOfMinutes)
}