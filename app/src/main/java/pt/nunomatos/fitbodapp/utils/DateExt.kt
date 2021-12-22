package pt.nunomatos.fitbodapp.utils

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private const val MMM_DD_FORMAT = "MMM dd"
private const val MMM_DD_YYYY_FORMAT = "MMM dd yyyy"

fun String?.convertToMMMDDYYYFormat(): String {
    if (isNullOrEmpty()) {
        return ""
    }

    val sourceDateFormat = SimpleDateFormat(MMM_DD_YYYY_FORMAT, Locale.getDefault())
    val targetFormat = SimpleDateFormat(MMM_DD_FORMAT, Locale.getDefault())

    return try {
        val date = sourceDateFormat.parse(this) ?: return ""
        targetFormat.format(date)
    } catch (exception: ParseException) {
        Log.d("DateExt", "Error: " + exception.message)
        ""
    }
}

fun String?.convertToDate(): Date {
    if (isNullOrEmpty()) {
        return Date()
    }

    val sourceDateFormat = SimpleDateFormat(MMM_DD_YYYY_FORMAT, Locale.getDefault())
    return try {
        sourceDateFormat.parse(this) ?: return Date()
    } catch (exception: ParseException) {
        Log.d("DateExt", "Error: " + exception.message)
        Date()
    }
}