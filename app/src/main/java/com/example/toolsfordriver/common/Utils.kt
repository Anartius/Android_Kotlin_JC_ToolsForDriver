package com.example.toolsfordriver.common

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.periodUntil
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.until
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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

fun calcPeriod(
    startDateTime: LocalDateTime?,
    endDateTime: LocalDateTime?,
    roundUpFromMinutes: Int
): DateTimePeriod? {
    return if (startDateTime != null && endDateTime != null) {
        val timeZone = TimeZone.currentSystemDefault()

        val start = roundFromMinutes(startDateTime, roundUpFromMinutes)
        val end = roundFromMinutes(endDateTime, roundUpFromMinutes)

        start.periodUntil(end, timeZone)
    } else null
}

fun roundFromMinutes(value: LocalDateTime, roundFrom: Int): Instant {
    val timeZone = TimeZone.currentSystemDefault()
    val minute = value.minute

    val addMinutes = if (minute >= roundFrom) 60 - minute  else -minute

    return value.toInstant(timeZone).plus(addMinutes, DateTimeUnit.MINUTE, timeZone)
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
    roundUpFromMinutes: Int,
    moneyPerHour: Double
): Double? {
    return if (startDateTime != null && endDateTime != null) {

        val start = roundFromMinutes(startDateTime, roundUpFromMinutes)
        val end = roundFromMinutes(endDateTime, roundUpFromMinutes)

        val duration = start.until(end, DateTimeUnit.HOUR).hours.inWholeHours
        ((moneyPerHour * duration) * 100).roundToInt() / 100.0
    } else null
}

fun getSelectableDateRange(startDate: LocalDate?): LongRange? {
    return if (startDate != null) {
        val timeZone = TimeZone.currentSystemDefault()
        val year = startDate.year
        val month = startDate.month
        val day = startDate.dayOfMonth

        val nextMonth = if (month == Month.DECEMBER) {
            LocalDate(year +1, 1, 1)
        } else LocalDate(year, month + 1, 1)

        val lastDayOfMonth = nextMonth.minus(DatePeriod(days = 1)).dayOfMonth

        val start = LocalDate(year, month, day).atStartOfDayIn(timeZone).toEpochMilliseconds()
        val end = LocalDate(year, month, lastDayOfMonth).atStartOfDayIn(timeZone)
            .toEpochMilliseconds()

        start..end
    } else null
}

fun saveBitmapToInternalStorage(bitmap: Bitmap, context: Context): Uri {
    var uri = "".toUri()

    val fileName = String.format(Locale.ROOT, "tmp%d.jpg", System.currentTimeMillis())

    try {
        val fos: FileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos)
        fos.close()
        val file = File(context.filesDir, fileName)
        uri = file.toUri()
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(
            context,
            "Image wasn't saved to the phone. ${e.message}",
            Toast.LENGTH_LONG
        ).show()
    }

    return uri
}

fun saveImageToInternalStorage(uri: Uri, context: Context): Uri {
    val fileName = String.format(Locale.ROOT, "%d.jpg", System.currentTimeMillis())

    val inputStream = context.contentResolver.openInputStream(uri)
    val outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)

    inputStream?.use { input ->
        outputStream?.use { output ->
            input.copyTo(output)
        }
    }

    val outputFile = context.filesDir.resolve(fileName)

    return if (outputFile.exists()) { outputFile.toUri() } else "".toUri()
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