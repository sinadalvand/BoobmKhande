package vortex.jokbazaar.Core.Xpack

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import vortex.jokbazaar.Core.Security.Offline.Reactor
import io.reactivex.disposables.Disposable
import javax.inject.Inject

abstract class XviewModel : ViewModel() {

    protected val disposables = arrayListOf<Disposable>()

    @Inject
    lateinit var reactor: Reactor



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