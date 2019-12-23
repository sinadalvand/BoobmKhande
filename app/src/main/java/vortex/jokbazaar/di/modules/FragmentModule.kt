package vortex.jokbazaar.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vortex.jokbazaar.view.fragment.ErrorSheet
import vortex.jokbazaar.view.fragment.FavorFragment
import vortex.jokbazaar.view.fragment.MainFragment
import vortex.jokbazaar.view.fragment.SettingsFragment

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun getMainFragment(): MainFragment

    @ContributesAndroidInjector
    abstract fun getFavorFragment(): FavorFragment

    @ContributesAndroidInjector
    abstract fun getSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun getErrorFragment(): ErrorSheet

}