package com.silwek.cleverchat.viewmodels

import android.arch.lifecycle.MutableLiveData

/**
 * @author Silwèk on 13/01/2018
 */
class FirebaseLiveData<T>(val onActiveObserver: () -> Unit, val onInactiveObserver: () -> Unit) : MutableLiveData<T>() {
    override fun onActive() {
        super.onActive()
        onActiveObserver()
    }

    override fun onInactive() {
        super.onInactive()
        onInactiveObserver()
    }
}