package com.selasarimaji.perpus

import android.content.Context
import android.content.Context.MODE_PRIVATE
import java.text.SimpleDateFormat
import java.util.*

const val CONTENT_TYPE_KEY = "CONTENT_TYPE_KEY"
enum class ContentType {
    Book, Category, Kid, Borrow
}

fun Context.storeStringVal(key: String, value: String){
    val pref = getSharedPreferences(applicationContext.packageName, MODE_PRIVATE)
    pref.edit().putString(key, value).apply()
}

fun Context.getStringVal(key: String, defaultValue: String) : String{
    val pref = getSharedPreferences(applicationContext.packageName, MODE_PRIVATE)
    return pref.getString(key, defaultValue)
}

fun getCurrentDateString(dayAhead : Int) : String {
    val date = Calendar.getInstance().apply {
        add(Calendar.DATE, dayAhead)
    }
    return SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(date.time)
}

fun parseDateString(dateString : String) : Calendar{
    val cal = Calendar.getInstance()
    val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
    cal.time = sdf.parse(dateString)// all done
    return cal
}

fun String.capitalizeWords() =
    split(" ").map { it.capitalize() }.reduce { acc, s -> "$acc $s" }

