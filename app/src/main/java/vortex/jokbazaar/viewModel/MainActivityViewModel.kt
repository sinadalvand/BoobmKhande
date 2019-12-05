package vortex.jokbazaar.viewModel

import androidx.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import vortex.jokbazaar.core.xpack.XviewModel
import vortex.jokbazaar.model.Post
import vortex.jokbazaar.protocol.Repository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainActivityViewModel @Inject constructor() : XviewModel() {

    @Inject
    lateinit var repo: Repository

    var joks = MutableLiveData<ArrayList<Post>>()
    var ads = MutableLiveData<ArrayList<String>>()


    fun getJoks(page: Int, lastId: String) {
        disposables.add(repo.getPost(page, lastId).observeOn(Schedulers.io()).subscribe({
            if (it.data.size > 0)
                addToLiveArray(it.data.toTypedArray(), joks)
        }, {
            it.printStackTrace()
        }))
    }


    fun addToFavor(jok: Post) {
        disposables.add(repo.addToFavorit(jok).observeOn(Schedulers.io()).subscribe({

        }, {
            it.printStackTrace()
        }))
    }


    private fun <T> addToLiveArray(array: Array<T>, liveArray: MutableLiveData<ArrayList<T>>) {
        val oldArray = liveArray.value ?: arrayListOf()
        oldArray.addAll(array)
        liveArray.postValue(oldArray)
    }

    private fun <T> addToLiveArray(array: T, liveArray: MutableLiveData<ArrayList<T>>) {
        val oldArray = liveArray.value ?: arrayListOf()
        oldArray.add(array)
        liveArray.postValue(oldArray)
    }


    fun deleteFavor(id: String,delete:Boolean) {
        if (joks.value == null || joks.value?.size == 0) return
        val array = joks.value!!
        for (post in array) {
            if (post.id == id)
                post.favorit = !delete
        }
        joks.postValue(array)
    }
}