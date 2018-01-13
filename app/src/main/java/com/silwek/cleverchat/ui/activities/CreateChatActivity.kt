package com.silwek.cleverchat.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.silwek.cleverchat.R
import kotlinx.android.synthetic.main.activity_createchat.*

/**
 * @author Silwèk on 13/01/2018
 */
class CreateChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createchat)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
