package com.silwek.cleverchat.presenters

import com.silwek.cleverchat.databases.CoreDatabaseFactory
import com.silwek.cleverchat.getDatabaseFactory
import com.silwek.cleverchat.models.ChatUser

/**
 * @author Silwèk on 31/01/2018
 */
class CreateChatPresenter(private val dbFactory: CoreDatabaseFactory = getDatabaseFactory()) {

    fun createChat(chatName: String, members: List<ChatUser>, onChatReady: (String) -> Unit) {
        val user = dbFactory.getUserDatabase()?.getCurrentUser()
        val chatMembers = members.toMutableList()
        chatMembers.add(ChatUser(user?.name, user?.id))
        dbFactory.getChatDatabase()?.createChat(chatName, chatMembers, onChatReady)
    }
}