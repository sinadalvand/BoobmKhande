package vortex.jokbazaar.protocol

import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query
import vortex.jokbazaar.model.Advertise
import vortex.jokbazaar.model.BoombBody
import vortex.jokbazaar.model.Post
import vortex.jokbazaar.model.Pagination

interface MainApiInterface {

    @GET("courses")
    fun getPosts(@Query("p") page: Int): Flowable<BoombBody>




}