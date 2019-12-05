package co.rosemovie.app.Di.Module


import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import vortex.jokbazaar.core.database.HandsomeDatabse
import vortex.jokbazaar.core.utils.Const
import vortex.jokbazaar.core.utils.UnsafeOkHttpClient
import vortex.jokbazaar.protocol.MainApiInterface
import vortex.jokbazaar.protocol.Repository
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {


    @Provides
    fun getOkHttp(): OkHttpClient {
        return UnsafeOkHttpClient.getUnsafeOkHttpClient()
    }


    @Provides
    fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Const.MAIN_URL)//CONST_BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Provides
    @Singleton
    fun getCallInterface(retrofit: Retrofit): MainApiInterface {
        return retrofit.create(MainApiInterface::class.java)
    }


    @Provides
    @Singleton
    fun getRepository(api: MainApiInterface,db: HandsomeDatabse): Repository {
        return Repository(api,db)
    }


}