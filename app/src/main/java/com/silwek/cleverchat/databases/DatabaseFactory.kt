package com.silwek.cleverchat.databases

/**
 * @author Silwèk on 12/01/2018
 */
class DatabaseFactory : CoreDatabaseFactory() {

    companion object {
        val USER_DATABASE: IUserDatabase by lazy {
            UserDatabase()
        }
        val FRIENDS_DATABASE: IFriendsDatabase by lazy {
            FriendsDatabase()
        }
    }

    override fun getUserDatabase(): IUserDatabase? {
        return USER_DATABASE
    }

    override fun getFriendsDatabase(): IFriendsDatabase? {
        return FRIENDS_DATABASE
    }

}