package vortex.jokbazaar.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import vortex.jokbazaar.BoombApplication
import vortex.jokbazaar.di.Module.ViewModelModule
import vortex.jokbazaar.di.modules.ActivityModule
import vortex.jokbazaar.di.modules.FragmentModule
import vortex.jokbazaar.di.modules.ApplicationModule
import javax.inject.Singleton


@Component(modules = [
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    ActivityModule::class,
    FragmentModule::class,
    ViewModelModule::class,
    ApplicationModule::class
])
@Singleton
interface MainComponent : AndroidInjector<BoombApplication> {

    @Component.Factory
    interface Factory {
        fun build(@BindsInstance context: Context): MainComponent
    }
}