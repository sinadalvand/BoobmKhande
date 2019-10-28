package vortex.jokbazaar.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vortex.jokbazaar.view.fragment.MainFragment

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun getMainFragment(): MainFragment
}