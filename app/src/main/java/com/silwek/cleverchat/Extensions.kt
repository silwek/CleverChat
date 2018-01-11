package com.silwek.cleverchat

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

/**
 * Created by Amandine Ferrand on 11/01/2018.
 */

fun AppCompatActivity.setActionBarTitle(actionBarTitle: String) {
    supportActionBar?.title = actionBarTitle
}

fun Fragment.getCompatActivity(): AppCompatActivity? {
    return activity as AppCompatActivity?
}