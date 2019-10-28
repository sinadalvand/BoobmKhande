package vortex.jokbazaar

import androidx.multidex.MultiDex
import co.ronash.pushe.Pushe
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
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
    }


    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerMainComponent.factory().build(this)
    }

}