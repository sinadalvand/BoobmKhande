package co.rosemovie.app.Core.Xpack

import android.app.Service
import android.content.Context
import co.rosemovie.app.Core.Security.Offline.Reactor
import dagger.android.AndroidInjection
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import javax.inject.Inject

abstract class Xservice :Service() {

    @Inject
    lateinit var reactor: Reactor

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
}