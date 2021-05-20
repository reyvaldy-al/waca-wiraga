package com.gachateam.wacawiraga.utils

import java.util.*

object Helper {
    fun getGreetingMessage():String{
        val c = Calendar.getInstance()

        return when (c.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "Selamat Pagi,"
            in 12..15 -> "Selamat Siang,"
            in 16..20 -> "Selamat Sore,"
            in 21..23 -> "Selamat Malam,"
            else -> "Hello"
        }
    }
}