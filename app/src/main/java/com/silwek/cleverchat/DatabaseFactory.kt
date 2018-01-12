package com.silwek.cleverchat

import com.silwek.cleverchat.databases.FirebaseDatabase

/**
 * @author Silwèk on 12/01/2018
 */
class DatabaseFactory {
    companion object {
        val firebaseDatabase: FirebaseDatabase by lazy {
            FirebaseDatabase()
        }
    }
}