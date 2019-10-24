package vortex.jokbazaar

import android.app.Application
import androidx.multidex.MultiDex
import co.ronash.pushe.Pushe
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class BoombApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // pushe init
        Pushe.initialize(this, true)

        // multi dex
        MultiDex.install(this)

        // Timber Log Init
        Timber.plant(Timber.DebugTree())

        // font setting
        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/dana.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
    }
}