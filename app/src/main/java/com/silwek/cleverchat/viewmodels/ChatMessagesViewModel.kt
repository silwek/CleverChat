package com.silwek.cleverchat.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.silwek.cleverchat.databases.DatabaseFactory
import com.silwek.cleverchat.models.ChatMessage
import com.silwek.cleverchat.notNull

/**
 * @author Silwèk on 13/01/2018
 */
class ChatMessagesViewModel() : ViewModel() {
    var chatId: String? = null
    private var chatMessages: FirebaseLiveData<List<ChatMessage>>? = null
    fun getChatMessages(): LiveData<List<ChatMessage>>? {
        if (chatMessages == null) {
            chatMessages = FirebaseLiveData<List<ChatMessage>>(
                    onActiveObserver = {
                        loadMessages()
                    },
                    onInactiveObserver = {
                        DatabaseFactory.firebaseDatabase.stopGetChatMessages()
                    })

        }
        return chatMessages
    }

    private fun loadMessages() {
        chatId.notNull {
            DatabaseFactory.firebaseDatabase.getChatMessages(it,
                    onMessageAdded = { chatMessage, previousId ->
                        val messages = chatMessages?.value?.toMutableList() ?: ArrayList(1)
                        if (!messages.contains(chatMessage)) {
                            val previous = messages.find { it.id == previousId }
                            when (previous) {
                                null -> messages.add(chatMessage)
                                else -> messages.add(messages.indexOf(previous), chatMessage)
                            }
                        }
                        chatMessages?.value = messages
                    },
                    onMessageRemoved = { chatMessage, removedId ->
                        val messages = chatMessages?.value?.toMutableList() ?: ArrayList(0)
                        if (messages.contains(chatMessage))
                            messages.remove(chatMessage)
                        chatMessages?.value = messages
                    })
        }
    }
}