package com.silwek.cleverchat.dummy

import java.util.*
import kotlin.collections.ArrayList

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object DummyContent {

    val CHATROOMS: MutableList<DummyChat> = ArrayList<DummyChat>()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val CHATROOMS_MAP: MutableMap<String, DummyChat> = HashMap()

    private val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addChat(i)
        }
    }

    private fun addChat(position: Int) {
        val chat = createDummyChat(position);
        CHATROOMS.add(chat)
        CHATROOMS_MAP.put(chat.id, chat)
    }

    private fun createDummyMessages(count: Int): List<DummyMessage> {
        val messages = ArrayList<DummyMessage>(count)
        for (i in 1..COUNT) {
            messages.add(createDummyMessage(i))
        }
        return messages
    }

    private fun createDummyMessage(position: Int): DummyMessage {
        return DummyMessage(position.toString(), "Message " + position)
    }

    private fun createDummyChat(position: Int): DummyChat {
        return DummyChat(position.toString(), "Chat " + position, createDummyMessages(position))
    }

    data class DummyChat(val id: String, val title: String, val messages: List<DummyMessage>) {
        override fun toString(): String = title
    }

    data class DummyMessage(val id: String, val content: String) {
        override fun toString(): String = content
    }
}
