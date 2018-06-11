package com.selasarimaji.perpus

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import com.esafirm.imagepicker.features.ImagePicker
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

fun Int.addZeroIfBelow10() =
       if (this > 9) this.toString() else "0$this"

fun TextInputLayout.tryToRemoveFromList(list: MutableList<TextInputLayout>) =
    this.editText?.text.toString().toLowerCase().also {
        if (it.isNotEmpty()) {
            list.remove(this)
        }
    }

fun ImagePicker.startImagePicker() =
    this.folderMode(true) // folder mode (false by default)
        .toolbarFolderTitle("Folder") // folder selection title
        .toolbarImageTitle("Tap to select") // image selection title
        .single() // single mode
        .theme(R.style.CustomImagePickerTheme) // must inherit ef_BaseTheme. please refer to sample
        .showCamera(true) // show camera or not (true by default)
        .start() // start image picker activity with request code

fun Int.resDrawable(context: Context) =
    ContextCompat.getDrawable(context, this)

fun Int.resColor(context: Context) =
    ContextCompat.getColor(context, this)

fun Drawable.tint(@ColorInt colorInt: Int) =
    DrawableCompat.setTint(this, colorInt)
