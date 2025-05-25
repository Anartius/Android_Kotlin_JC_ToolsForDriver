package com.example.toolsfordriver.common

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun dateAsString(dateTime: Long?): String {
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.ROOT)
    return if (dateTime != null) {
        formatter.format(Date(dateTime))
    } else ""
}

fun dateAsString(dateTime: Date?): String {
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.ROOT)
    return if (dateTime != null) {
        formatter.format(dateTime)
    } else ""
}

fun dateAsString(dateTime: LocalDateTime?): String {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ROOT)
    return if (dateTime != null) {
        formatter.format(dateTime)
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

fun roundFromMinutes(value: LocalDateTime, roundFrom: Int): LocalDateTime {
    val minute = value.minute.toLong()
    val amountOfMinutes = if (minute >= roundFrom) 60 - minute  else -minute

    return value.plusMinutes(amountOfMinutes)
}

fun durationAsString(duration: Duration): String {
    val days = duration.toDays()
    val hours = duration.toHours() % 24

    return ((if (days != 0L) "$days d " else "") + "$hours h")
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

//fun deleteImageFromInternalStorage(
//    uri: Uri,
//    context: Context
//): Boolean {
//    return try {
//        val directory = context.filesDir
//        val fileName = uri.pathSegments.last()
//        val file = File(directory, fileName)
//        file.delete()
//        true
//    } catch (e: Exception) {
//        Log.e("Image delete", e.message.toString())
//        Toast.makeText(context, "Image wasn't delete", Toast.LENGTH_LONG).show()
//        false
//    }
//}