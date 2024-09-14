package com.example.toolsfordriver.common

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
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