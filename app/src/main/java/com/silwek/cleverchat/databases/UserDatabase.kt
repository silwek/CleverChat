package com.silwek.cleverchat.databases

import com.google.firebase.auth.FirebaseAuth
import com.silwek.cleverchat.models.User

/**
 * @author Silwèk on 15/01/2018
 */
class UserDatabase : IUserDatabase {
    override fun getCurrentUser(): User? {
        val user = FirebaseAuth.getInstance().currentUser
        return when (user) {
            null -> null
            else -> User(user.displayName, user.uid, user)
        }
    }

    override fun getCurrentUserId(): String? {
        val user = FirebaseAuth.getInstance().currentUser
        return user?.uid
    }
}