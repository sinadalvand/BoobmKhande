package co.rosemovie.app.Core.Xpack

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.rosemovie.app.Core.Provider.Repository
import co.rosemovie.app.Core.Security.Offline.Reactor
import co.rosemovie.app.Protocol.MainApiInterface
import io.reactivex.disposables.Disposable
import javax.inject.Inject

abstract class XviewModel : ViewModel() {

    protected val disposables = arrayListOf<Disposable>()

    @Inject
    lateinit var reactor: Reactor

    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var api: MainApiInterface

    private var firstFetch = true

    fun isFirstFetch(): Boolean = firstFetch

    fun consumeFetch() {
        firstFetch = false
    }

    protected fun <T> hasValue(data: MutableLiveData<T>): Boolean {
        return data.value != null
    }

    protected fun <T> valueEqualment(newData: T, oldData: T): Boolean {
        return newData == oldData
    }

    override fun onCleared() {
        for (disposable in disposables)
            disposable.dispose()
        super.onCleared()
    }
}