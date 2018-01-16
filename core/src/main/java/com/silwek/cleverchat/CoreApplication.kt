package com.silwek.cleverchat

import android.app.Application
import android.content.Intent
import com.silwek.cleverchat.databases.CoreDatabaseFactory

/**
 * @author Silwèk on 15/01/2018
 */
abstract class CoreApplication : Application() {
    companion object {
        lateinit var instance: CoreApplication
        private val FACTORY_CORE: CoreDatabaseFactory by lazy {
            CoreDatabaseFactory()
        }

        fun getDatabaseFactory(): CoreDatabaseFactory {
            return instance.getDatabaseFactory()
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    open fun getDatabaseFactory(): CoreDatabaseFactory {
        return FACTORY_CORE
    }

    abstract fun getAccountIntent(): Intent
}