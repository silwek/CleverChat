package com.silwek.cleverchat.databases

import com.silwek.cleverchat.models.User

/**
 * @author Silwèk on 15/01/2018
 */

interface IUserDatabase {
    fun getCurrentUser(): User?

    fun getCurrentUserId(): String?

}