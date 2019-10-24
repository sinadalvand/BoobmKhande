package co.rosemovie.app.Core.Security.Offline

import android.content.Context
import co.rosemovie.app.BuildConfig
import com.securepreferences.SecurePreferences

class Reactor(context: Context, key: String) {

    // add  buildConfigField "String", "appName", 'rosemovie' to defaultConfig in gradle
    // add  implementation 'com.scottyab:secure-preferences-lib:0.1.7' to dependency

//    private val securePref = SecurePreferences(context, key, "${BuildConfig.appName}.xml")
    private val securePref =context.getSharedPreferences(BuildConfig.appName,Context.MODE_PRIVATE)
    private val editor = securePref.edit()

    fun get(key: String, default: String): String {
        return securePref.getString(key, default) ?: default
    }


    fun get(key: String, default: Boolean): Boolean {
        return securePref.getBoolean(key, default)
    }


    fun get(key: String, default: Int): Int {
        return securePref.getInt(key, default)
    }


    fun get(key: String, default: Float): Float {
        return securePref.getFloat(key, default)
    }


    fun put(key: String, value: String) {
        editor.putString(key, value)
        apply()
    }


    fun put(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        apply()
    }


    fun put(key: String, value: Int) {
        editor.putInt(key, value)
        apply()
    }


    fun put(key: String, value: Float) {
        editor.putFloat(key, value)
        apply()
    }


    private fun apply() {
        editor.apply()
    }


    fun remove(key: String, useless: String = "") {
        editor.remove(key)
        editor.commit()
    }

}