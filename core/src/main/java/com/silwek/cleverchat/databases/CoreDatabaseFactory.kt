package com.silwek.cleverchat.databases

/**
 * @author Silwèk on 12/01/2018
 */
open class CoreDatabaseFactory {
    companion object {
        val firebaseDatabase: IChatDatabase by lazy {
            FirebaseDatabase()
        }
    }

    open fun getChatDatabase(): IChatDatabase? {
        return firebaseDatabase
    }

    open fun getUserDatabase(): IUserDatabase? {
        return null
    }

    open fun getFriendsDatabase(): IFriendsDatabase? {
        return null
    }
}