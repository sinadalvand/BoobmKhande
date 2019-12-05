package vortex.jokbazaar.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.res.Resources
import android.content.res.Resources.NotFoundException
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.TypedValue
import androidx.core.content.ContextCompat
import vortex.jokbazaar.BuildConfig
import vortex.jokbazaar.R
import java.util.regex.Pattern


object Utils {


     fun dp2px(context: Context,dp: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        )
    }

    fun shareText(text: String, context: Context, subject: String = context.getString(R.string.share_link)) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(Intent.createChooser(sharingIntent, subject))
    }


    fun getApplicationSign(context: Context): String = BuildConfig.VERSION_NAME

    fun virateNow(context: Context) {
        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            v!!.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
        else
            v!!.vibrate(150)

    }

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
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            Resources.getSystem().displayMetrics
        )
            .toInt()
    }

    internal fun pxToDp(px: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            px,
            Resources.getSystem().displayMetrics
        ).toInt()
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