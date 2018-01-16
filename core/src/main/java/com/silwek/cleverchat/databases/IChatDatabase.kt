package com.silwek.cleverchat.databases

import com.google.firebase.database.DatabaseReference
import com.silwek.cleverchat.models.ChatMessage
import com.silwek.cleverchat.models.ChatRoom
import com.silwek.cleverchat.models.ChatUser

/**
 * @author Silwèk on 15/01/2018
 */
interface IChatDatabase {

    fun getChatRoomsForUser(onResult: (List<ChatRoom>) -> Unit)

    fun createChat(name: String, members: List<ChatUser>, onResult: (String) -> Unit)

    fun sendMessage(chatId: String, content: String, onSuccess: () -> Unit)

    fun getChatMessages(chatId: String,
                        onMessageAdded: (ChatMessage, String?) -> Unit,
                        onMessageRemoved: (ChatMessage, String?) -> Unit)

    fun stopGetChatMessages()

    fun getDatabase(): DatabaseReference

    companion object {
        val NODE_ROOT = "chat"
        val NODE_CHATS = "chats"
        val NODE_MEMBERS = "members"
        val NODE_USERS = "users"
        val NODE_MESSAGES = "messages"
    }
}

