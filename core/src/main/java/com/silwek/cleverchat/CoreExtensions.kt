package com.silwek.cleverchat

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.silwek.cleverchat.databases.CoreDatabaseFactory

/**
 * @author Silwèk on 12/01/2018
 */

fun AppCompatActivity.setActionBarTitle(actionBarTitle: String) {
    supportActionBar?.title = actionBarTitle
}

fun Fragment.getCompatActivity(): AppCompatActivity? {
    return activity as AppCompatActivity?
}

fun Any.getDatabaseFactory(): CoreDatabaseFactory {
    return CoreApplication.getDatabaseFactory()
}


fun <T : Any> T?.notNull(f: (it: T) -> Unit) {
    if (this != null) f(this)
}