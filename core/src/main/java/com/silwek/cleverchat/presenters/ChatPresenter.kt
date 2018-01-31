package com.silwek.cleverchat.presenters

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.silwek.cleverchat.databases.CoreDatabaseFactory
import com.silwek.cleverchat.getDatabaseFactory
import com.silwek.cleverchat.models.ChatMessage
import com.silwek.cleverchat.viewmodels.ChatMessagesViewModel

/**
 * @author Silwèk on 31/01/2018
 */
class ChatPresenter(private val dbFactory: CoreDatabaseFactory = getDatabaseFactory()) {
    fun getUserId(): String? = dbFactory.getUserDatabase()?.getCurrentUserId()

    fun getChatMessages(chatId: String, owner: LifecycleOwner, onMessages: (List<ChatMessage>?) -> Unit) {
        val model = when (owner) {
            is FragmentActivity -> ViewModelProviders.of(owner).get(ChatMessagesViewModel::class.java)
            is Fragment -> ViewModelProviders.of(owner).get(ChatMessagesViewModel::class.java)
            else -> throw IllegalArgumentException("Only Fragment and FragmentActivity are supported")
        }
        model.chatId = chatId
        model.getChatMessages()?.observe(owner, Observer<List<ChatMessage>?> { messages -> onMessages(messages) })
    }

    fun sendMessage(chatId: String, message: String, onSuccess: () -> Unit) {
        dbFactory.getChatDatabase()?.sendMessage(chatId, message, onSuccess)
    }
}