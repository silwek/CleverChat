package com.silwek.cleverchat.utils

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import org.jetbrains.anko.AnkoException
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * @author Silwèk on 30/01/2018
 */
object UiUtils {
    /**
     * <p>Create a Fragment and filled arguments from an Array of Pair.</p>
     * <pre>UiUtils.createFragment<MyFragment>(KEY, value, KEY2, value2)</pre>
     * <p>Or with "to" keyword</p>
     * <pre>UiUtils.createFragment<MyFragment>(KEY to value, KEY2 to value2)</pre>
     */
    fun createFragment(clazz: KClass<out Fragment>, params: Array<out Pair<String, Any?>>): Fragment {
        val fragment: Fragment = clazz.createInstance()
        if (params.isNotEmpty()) {
            fragment.apply {
                arguments = fillBundle(params)
            }
        }
        return fragment
    }

    /**
     * <p>Fill a bundle with an Array of Pair.</p>
     * <pre>UiUtils.fillBundle(KEY, value, KEY2, value2)</pre>
     * <p>Or with "to" keyword</p>
     * <pre>UiUtils.fillBundle(KEY to value, KEY2 to value2)</pre>
     */
    fun fillBundle(params: Array<out Pair<String, Any?>>): Bundle {
        val bundle = Bundle()
        params.forEach {
            val value = it.second
            when (value) {
                null -> bundle.putSerializable(it.first, null as java.io.Serializable?)
                is Int -> bundle.putInt(it.first, value)
                is Long -> bundle.putLong(it.first, value)
                is CharSequence -> bundle.putCharSequence(it.first, value)
                is String -> bundle.putString(it.first, value)
                is Float -> bundle.putFloat(it.first, value)
                is Double -> bundle.putDouble(it.first, value)
                is Char -> bundle.putChar(it.first, value)
                is Short -> bundle.putShort(it.first, value)
                is Boolean -> bundle.putBoolean(it.first, value)
                is java.io.Serializable -> bundle.putSerializable(it.first, value)
                is Bundle -> bundle.putBundle(it.first, value)
                is Parcelable -> bundle.putParcelable(it.first, value)
                is Array<*> -> when {
                    value.isArrayOf<CharSequence>() -> bundle.putCharSequenceArray(it.first, value as Array<out CharSequence>?)
                    value.isArrayOf<String>() -> bundle.putStringArray(it.first, value as Array<out String>?)
                    value.isArrayOf<Parcelable>() -> bundle.putParcelableArray(it.first, value as Array<out Parcelable>?)
                    else -> throw AnkoException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
                }
                is IntArray -> bundle.putIntArray(it.first, value)
                is LongArray -> bundle.putLongArray(it.first, value)
                is FloatArray -> bundle.putFloatArray(it.first, value)
                is DoubleArray -> bundle.putDoubleArray(it.first, value)
                is CharArray -> bundle.putCharArray(it.first, value)
                is ShortArray -> bundle.putShortArray(it.first, value)
                is BooleanArray -> bundle.putBooleanArray(it.first, value)
                else -> throw AnkoException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
            }
        }
        return bundle
    }
}