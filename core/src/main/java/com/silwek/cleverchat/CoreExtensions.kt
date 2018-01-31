package com.silwek.cleverchat

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.silwek.cleverchat.databases.CoreDatabaseFactory
import com.silwek.cleverchat.ui.activities.ToolbarManager
import com.silwek.cleverchat.utils.UiUtils
import org.jetbrains.anko.internals.AnkoInternals

/**
 * @author Silwèk on 12/01/2018
 */

fun Fragment.getCompatActivity(): AppCompatActivity? {
    return activity as AppCompatActivity?
}

fun Fragment.getToolbarManager(): ToolbarManager? {
    return activity as ToolbarManager?
}

fun getDatabaseFactory(): CoreDatabaseFactory {
    return CoreApplication.getDatabaseFactory()
}

fun <T : Any> T?.notNull(f: (it: T) -> Unit) {
    if (this != null) f(this)
}

fun <K, V : Any> Map<K, V?>.toVarargArray(): Array<out Pair<K, V>> =
        map({ Pair(it.key, it.value!!) }).toTypedArray()

inline fun <reified T : Fragment> fragmentFor(vararg params: Pair<String, Any?>): Fragment =
        UiUtils.createFragment(T::class, params)

inline fun <reified T : Fragment> fragmentFor(params: Map<String, Any?>): Fragment =
        UiUtils.createFragment(T::class, params.toVarargArray())

inline fun <reified T : Activity> Context.startActivity(params: Map<String, Any?>) =
        AnkoInternals.internalStartActivity(this, T::class.java, params.toVarargArray())

fun AppCompatActivity.addFragment(fragment: Fragment, containerId: Int) {
    supportFragmentManager.beginTransaction()
            .add(containerId, fragment)
            .commit()
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, containerId: Int) {
    supportFragmentManager.beginTransaction()
            .replace(containerId, fragment)
            .commit()
}


