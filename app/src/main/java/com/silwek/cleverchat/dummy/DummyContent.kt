package com.silwek.cleverchat.dummy

import com.silwek.cleverchat.models.ChatMessage

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object DummyContent {


    val CHATMESSAGES: MutableList<ChatMessage> = ArrayList<ChatMessage>()

    private val COUNT = 25

    init {
        createDummyMessages()
    }

    private fun createDummyMessages() {
        for (i in 1..COUNT) {
            CHATMESSAGES.add(createDummyMessage(i))
        }
    }

    private fun createDummyMessage(position: Int): ChatMessage {
        return ChatMessage(message = "Message " + position, id = position.toString())
    }
}
