package com.example.toolsfordriver.common

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.util.Locale

fun getSpecificMonthRange(yearMonth: YearMonth): String {

    val start = LocalDate.of(yearMonth.year, yearMonth.month, 1)

    val end = LocalDate.of(
        yearMonth.year, yearMonth.month, yearMonth.month.length(yearMonth.isLeapYear)
    )

    return "$start, $end"
}

fun getRangeAsString(start: Long, end: Long): String {
    val zoneId = ZoneId.systemDefault()

    val start = Instant.ofEpochMilli(start).atZone(zoneId).toLocalDate()
    val end = Instant.ofEpochMilli(end).atZone(zoneId).toLocalDate()

    return "$start, $end"
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