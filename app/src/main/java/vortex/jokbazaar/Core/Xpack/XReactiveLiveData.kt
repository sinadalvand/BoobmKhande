package vortex.jokbazaar.Core.Utils

import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.atomic.AtomicBoolean
import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.lifecycle.*


class XReactiveLiveData<T>(val singleSend: Boolean = false) : MutableLiveData<T>() {

    private var atomic: AtomicReference<FuckingSubscriber> = AtomicReference()
    private val mPending = AtomicBoolean(false)

    override fun onActive() {
        super.onActive()
    }

    override fun onInactive() {
        super.onInactive()

        val subscriber = atomic.getAndSet(null)
        subscriber?.let {
            subscriber.cancellSubscrib()
        }
    }

    fun request(publisher: Publisher<T>) {
        val subscriber = atomic.getAndSet(null)
        subscriber?.let {
            subscriber.cancellSubscrib()

        }

        atomic.set(FuckingSubscriber())

        if (atomic.get() != null) {
            publisher.subscribe(atomic.get())
        }

    }

    @MainThread
    override fun observe(@NonNull owner: LifecycleOwner, @NonNull observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Log.w("ReactiveError", "Multiple observers registered but only one will be notified of changes.")
        }

        // Observe the internal MutableLiveData
        super.observe(owner, Observer<T> { t ->
            if (singleSend) {
                if (mPending.compareAndSet(true, false)) {
                    observer.onChanged(t)
                }
            } else {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(@Nullable t: T) {
        mPending.set(true)
        super.setValue(t)
    }

    override fun postValue(value: T) {
        mPending.set(true)
        super.postValue(value)
    }


    inner class FuckingSubscriber : AtomicReference<Subscription>(), Subscriber<T> {

        override fun onComplete() {
            atomic.compareAndSet(this, null)
        }

        override fun onSubscribe(s: Subscription?) {
            if (compareAndSet(null, s)) {
                s?.request(java.lang.Long.MAX_VALUE)
            } else {
                s?.cancel()
            }
        }

        override fun onNext(t: T) {
            postValue(t)
        }

        override fun onError(t: Throwable?) {
            Log.e("ReactiveLivedata","Error: ${t?.message}")
            atomic.compareAndSet(this, null)
        }

        fun cancellSubscrib() {
            val s = get()
            s?.let {
                s.cancel()
            }
        }

    }
}