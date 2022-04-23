package com.example.weatherapp.utils

import java.text.SimpleDateFormat
import java.util.*

object UtilConstants {

    const val SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS" // not timezone aware.
    const val DATE_DISPLAY_FORMAT = "dd MMMM yyyy"
    const val TIME_DISPLAY_FORMAT = "hh:mm a"
    const val FILTER_DATE_DISPLAY_FORMAT = "yyyy-MM-dd"
    const val DATE_TIME_DISPLAY_FORMAT = "dd MMMM yyyy, hh:mm a"
    const val MAPVIEW_BUNDLE_KEY = "mapview"
    const val DB_TIMER = "TIMER"
    const val PLATFORM_NAME = "mobile-android"
    const val MY_RETRIEVALS_KEY = "my_retrievals"
    const val ALL_RETRIEVALS_KEY = "all_retrievals"

}

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter
            = SimpleDateFormat(format, locale)
    return formatter.format(this)
}