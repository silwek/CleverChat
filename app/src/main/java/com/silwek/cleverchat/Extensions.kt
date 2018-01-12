package com.silwek.cleverchat

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.silwek.cleverchat.ui.activities.SplashscreenActivity

/**
 * Created by Amandine Ferrand on 11/01/2018.
 */

fun AppCompatActivity.setActionBarTitle(actionBarTitle: String) {
    supportActionBar?.title = actionBarTitle
}

fun Fragment.getCompatActivity(): AppCompatActivity? {
    return activity as AppCompatActivity?
}

fun AppCompatActivity.getLandingActivityIntent(): Intent {
    val i = Intent(this, SplashscreenActivity::class.java)
    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    return i
}

fun Fragment.getLandingActivityIntent(): Intent? {
    return getCompatActivity()?.getLandingActivityIntent()
}