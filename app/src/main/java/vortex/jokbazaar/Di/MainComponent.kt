package vortex.jokbazaar.Di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import vortex.jokbazaar.BoombApplication
import vortex.jokbazaar.Di.module.ApplicationModule
import vortex.jokbazaar.Di.Module.ViewModelModule

@Component(modules = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        ViewModelModule::class,
        ApplicationModule::class])
interface MainComponent : AndroidInjector<BoombApplication> {

    @Component.Factory
    interface Factory {
        fun build(@BindsInstance context: Context): MainComponent
    }
}