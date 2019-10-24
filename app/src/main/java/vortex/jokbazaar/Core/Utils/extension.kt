package co.rosemovie.app.Core.Utils

import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import java.math.BigInteger
import java.security.MessageDigest


fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun TextView.setFont(font: String, bold: Boolean = false) {
    val face = Typeface.createFromAsset(context.assets, "fonts/${font}.ttf")
    if (bold) typeface = face else setTypeface(face, Typeface.BOLD)
}

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}