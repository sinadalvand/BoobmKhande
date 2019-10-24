package co.rosemovie.app.Core.Utils

import android.content.Context
import android.content.res.Resources
import android.content.res.Resources.NotFoundException
import android.util.TypedValue
import androidx.core.content.ContextCompat
import android.graphics.Typeface.createFromAsset
import android.R.id.custom
import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import java.util.regex.Pattern


object Utils {

    fun getColorAttr(context: Context, attr: Int): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attr, typedValue, true)
        val colorRes = typedValue.resourceId
        var color = -1
        try {
            color = ContextCompat.getColor(context, colorRes)
        } catch (e: NotFoundException) {
            e.printStackTrace()
        }
        return color
    }

    internal fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), Resources.getSystem().displayMetrics)
            .toInt()
    }

    internal fun pxToDp(px: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, Resources.getSystem().displayMetrics).toInt()
    }


    fun openBrowser(activity: Activity, link: String) {
        val browserIntent = Intent(ACTION_VIEW, Uri.parse(link))
        activity.startActivity(browserIntent)
    }

    fun isEmailValid(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }

    fun fieldValidator(email: String, length: Int = 50): Boolean {
        return (email.length < length && email != "" && email != " ")
    }


}