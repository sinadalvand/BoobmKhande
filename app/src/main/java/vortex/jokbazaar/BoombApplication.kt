package vortex.jokbazaar

import androidx.multidex.MultiDex
import co.ronash.pushe.Pushe
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import ir.tapsell.sdk.Tapsell
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import vortex.jokbazaar.core.utils.Const
import vortex.jokbazaar.di.DaggerMainComponent

//import vortex.jokbazaar.Di.DaggerMainComponent

class BoombApplication : DaggerApplication() {

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


        Tapsell.initialize(this,BuildConfig.TAPSELL_NATIVE_BANNER)
    }


    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerMainComponent.factory().build(this)
    }

}