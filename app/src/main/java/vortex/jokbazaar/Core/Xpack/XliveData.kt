package co.rosemovie.app.Core.Xpack

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View.X
import androidx.annotation.MainThread
import androidx.annotation.Nullable
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import co.ronash.pushe.g.a.w
import android.view.View.X
import androidx.annotation.NonNull


class XliveData<X>(private val once:Boolean=false) : MutableLiveData<X>() {

    private val atomicBoolean = AtomicBoolean(false)

    @MainThread
    override fun observe(@NonNull owner: LifecycleOwner, @NonNull observer: Observer<in X>) {
        if (hasActiveObservers()) {
            Log.w("XliveData", "Multiple observers registered but only one will be notified of changes.")
        }

        // Observe the internal MutableLiveData
        super.observe(owner, Observer<X> { t ->
            if (once) {
                if (atomicBoolean.compareAndSet(true, false)) {
                    observer.onChanged(t)
                }
            } else {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override
    fun setValue(@Nullable t: X) {
        atomicBoolean.set(true)
        super.setValue(t)
    }

    override
    fun postValue(value: X) {
        atomicBoolean.set(true)
        super.postValue(value)
    }

}
