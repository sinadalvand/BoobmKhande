package vortex.jokbazaar.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vortex.jokbazaar.view.activity.MainActivity

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity

}