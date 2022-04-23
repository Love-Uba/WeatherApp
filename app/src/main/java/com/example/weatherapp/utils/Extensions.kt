package com.example.weatherapp.utils

import com.example.weatherapp.utils.UtilConstants.DATE_DISPLAY_FORMAT
import com.example.weatherapp.utils.UtilConstants.SERVER_DATE_FORMAT
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.getFormattedDateTime(displayFormat: String): String {

    val dateFormatIn = SimpleDateFormat(SERVER_DATE_FORMAT, Locale.getDefault())
    return try {
        val dateIn = dateFormatIn.parse(this)!!
        val offset = TimeZone.getDefault().getOffset(dateIn.time)
        val cal = Calendar.getInstance().apply {
            time = dateIn
            add(Calendar.MILLISECOND, offset)
        }
        val dateFormatOut = SimpleDateFormat(displayFormat, Locale.getDefault())
        dateFormatOut.format(cal.time)
    }catch (exception: ParseException) {
        SimpleDateFormat(displayFormat, Locale.getDefault()).format(Date())
    } catch (exception: Exception) {
        SimpleDateFormat(displayFormat, Locale.getDefault()).format(Date())
    }
}
