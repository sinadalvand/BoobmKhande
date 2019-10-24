package vortex.jokbazaar.Di.Module

import androidx.lifecycle.ViewModelProvider
import vortex.jokbazaar.Core.Xpack.ViewModelFactory
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class ViewModelModule {

    @Binds
    @Singleton
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


//    @Binds
//    @IntoMap
//    @ViewModelKey(MainActivityViewModel::class)
//    internal abstract fun mainActivityViewModel(activityViewModel: MainActivityViewModel): ViewModel



}