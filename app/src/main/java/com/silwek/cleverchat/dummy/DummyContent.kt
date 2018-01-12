package com.silwek.cleverchat.dummy

import com.silwek.cleverchat.models.ChatMessage
import com.silwek.cleverchat.models.ChatRoom
import java.util.*
import kotlin.collections.ArrayList

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object DummyContent {

    val CHATMESSAGES: MutableList<ChatMessage> = ArrayList<ChatMessage>()

    val CHATROOMS: MutableList<ChatRoom> = ArrayList<ChatRoom>()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val CHATROOMS_MAP: MutableMap<String, ChatRoom> = HashMap()

    private val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addChat(i)
        }
        createDummyMessages(COUNT)
    }

    private fun addChat(position: Int) {
        val chat = createDummyChat(position)
        CHATROOMS.add(chat)
        if (chat.id != null)
            CHATROOMS_MAP.put(chat.id!!, chat)

    }

    private fun createDummyMessages(count: Int) {
        for (i in 1..COUNT) {
            CHATMESSAGES.add(createDummyMessage(i))
        }
    }

    private fun createDummyMessage(position: Int): ChatMessage {
        return ChatMessage(message = "Message " + position, id = position.toString())
    }

    private fun createDummyChat(position: Int): ChatRoom {
        return ChatRoom(id = position.toString(), name = "Chat " + position)
    }

    data class DummyChat(val id: String, val title: String, val messages: List<DummyMessage>) {
        override fun toString(): String = title
    }

    data class DummyMessage(val id: String, val content: String) {
        override fun toString(): String = content
    }
}
