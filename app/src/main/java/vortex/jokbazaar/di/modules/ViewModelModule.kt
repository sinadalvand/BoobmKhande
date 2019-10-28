package vortex.jokbazaar.di.Module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vortex.jokbazaar.core.xpack.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import vortex.jokbazaar.di.Keys.ViewModelKey
import vortex.jokbazaar.viewModel.MainActivityViewModel
import vortex.jokbazaar.viewModel.MainFragmentViewModel
import javax.inject.Singleton

@Module
abstract class ViewModelModule {

    @Binds
    @Singleton
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    @Singleton
    internal abstract fun mainActivityViewModel(activityViewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainFragmentViewModel::class)
    @Singleton
    internal abstract fun mainFragmentViewModel(mainFragmentViewModel: MainFragmentViewModel): ViewModel


}