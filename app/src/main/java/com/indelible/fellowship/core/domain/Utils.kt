package com.indelible.fellowship.core.domain

import java.text.SimpleDateFormat
import java.util.Date


fun convertLongToTime(time: Long): String{
    val date = Date(time)
    val format = SimpleDateFormat("HH:mm")
    return format.format(date)
}

fun convertLongToDate(time: Long?): String{
    return if (time == null){
        ""
    }else{
        val date = Date(time)
        val format = SimpleDateFormat("dd MMM")
        format.format(date)
    }

}