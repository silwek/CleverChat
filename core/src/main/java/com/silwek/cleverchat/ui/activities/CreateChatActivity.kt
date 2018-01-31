package com.silwek.cleverchat.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.silwek.cleverchat.R
import org.jetbrains.anko.find

/**
 * @author Silwèk on 13/01/2018
 */
class CreateChatActivity : AppCompatActivity(), ToolbarManager {
    override val toolbar: Toolbar by lazy { find<Toolbar>(R.id.toolbar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createchat)
        setSupportActionBar(toolbar)
        enableHomeAsUp { onBackPressed() }
    }

}
