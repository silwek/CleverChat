package com.silwek.cleverchat

import android.content.Intent
import com.silwek.cleverchat.databases.CoreDatabaseFactory
import com.silwek.cleverchat.databases.DatabaseFactory
import com.silwek.cleverchat.ui.activities.AccountActivity

/**
 * @author Silwèk on 15/01/2018
 */
class CleverChatApplication : CoreApplication() {
    companion object {
        private val FACTORY: CoreDatabaseFactory by lazy {
            DatabaseFactory()
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun getDatabaseFactory(): CoreDatabaseFactory {
        return FACTORY
    }

    override fun getAccountIntent(): Intent {
        return Intent(this, AccountActivity::class.java)
    }
}