package com.silwek.cleverchat.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.silwek.cleverchat.R
import kotlinx.android.synthetic.main.activity_account.*

/**
 * @author Silwèk on 12/01/2018
 */
class AccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
