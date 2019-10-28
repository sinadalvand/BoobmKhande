package vortex.jokbazaar.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import vortex.jokbazaar.core.database.HandsomeDatabse
import vortex.jokbazaar.core.security.offline.Reactor
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @Singleton
    fun getReactor(context: Context): Reactor = Reactor(context, "aS5a1H7a")

    @Provides
    @Singleton
    fun getDatabse(context: Context): HandsomeDatabse = HandsomeDatabse.invoke(context)


}