package com.gachateam.wacawiraga.utils

import android.content.Context
import android.widget.Toast
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

    val labels = listOf(
        "a",
        "b",
        "c",
        "d",
        "e",
        "f",
        "g",
        "h",
        "i",
        "j",
        "k",
        "l",
        "m",
        "n",
        "o",
        "p",
        "q",
        "r",
        "s",
        "t",
        "u",
        "v",
        "w",
        "x",
        "y",
        "z"
        )
}

fun Context.toast(message: String, duration : Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this,message,duration).show()
}

fun getImageTextFromIndex(index: Int): String {
    return when (index) {
        0 -> "rumah"
        1 -> "sekolah"
        else -> "not found"
    }
}