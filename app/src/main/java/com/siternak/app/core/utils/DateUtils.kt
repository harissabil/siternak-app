package com.siternak.app.core.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/*
Use this class to format date string from API response.
Desired format: yyyy-MM-dd
 */

object DateUtils {
    fun formatDate(dateString: String): String {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val desiredFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date: Date = originalFormat.parse(dateString) as Date

        return desiredFormat.format(date)
    }
}

fun Timestamp.toDateYyyyMmDd(): String {
    val date = this.toDate()
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(date)
}