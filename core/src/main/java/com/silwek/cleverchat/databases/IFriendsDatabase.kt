package com.silwek.cleverchat.databases

import com.silwek.cleverchat.models.ChatUser

/**
 * @author Silwèk on 15/01/2018
 */
interface IFriendsDatabase {

    fun insertChatUserIfNeeded()

    fun getUsers(onResult: (List<ChatUser>) -> Unit)
}