package vortex.jokbazaar.protocol

import com.google.android.gms.common.util.ArrayUtils
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Timed
import timber.log.Timber
import vortex.jokbazaar.core.database.HandsomeDatabse
import vortex.jokbazaar.model.BoombBody
import vortex.jokbazaar.model.Post
import vortex.jokbazaar.model.Pagination

class Repository(private val api: MainApiInterface, private val database: HandsomeDatabse) {

    fun getPost(page: Int, lastId: String): Flowable<Pagination<Post>> {
        var errorHappened = false
        return api.getPosts(page).subscribeOn(Schedulers.io())
                .onErrorReturn {
                    errorHappened = true
                    val data = BoombBody()
                    val secondData = Pagination<Post>()
                    secondData.data.addAll(getLazyJok(lastId))
                    secondData.currentPage = secondData.currentPage
                    data.data = secondData
                    return@onErrorReturn data
                }.flatMap {
                    if (errorHappened) return@flatMap Flowable.just(it.data)

                    database.getMainDAO().insertJoks(it.data.data.toTypedArray()).blockingGet()

                    val secondData = Pagination<Post>()
                    val data = database.getMainDAO().getJokFrom(it.data.data[0].id).blockingFirst()
                    secondData.data = ArrayUtils.toArrayList(data)

                    return@flatMap Flowable.just(secondData)
                }
    }


    fun addToFavorit(jok: Post): Flowable<Long> {
        jok.favorit = true
        return database.getMainDAO().insertFavoritJok(jok).subscribeOn(Schedulers.io()).toFlowable()
    }


    fun getLazyJok(lastid: String): Array<Post> {
        return database.getMainDAO().getJokFrom(lastid).blockingFirst()
    }

    fun getFavor(): Array<Post> {
        return database.getMainDAO().getFavoritJok().blockingFirst()
    }


}