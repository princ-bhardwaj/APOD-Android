package com.example.apod.utils

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object Util {

    fun getCurrentDate(format : String ) : String{
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format).withZone(ZoneId.systemDefault()))
    }
}