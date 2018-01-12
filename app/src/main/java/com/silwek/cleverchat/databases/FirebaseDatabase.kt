package com.silwek.cleverchat.databases

import com.google.firebase.auth.FirebaseUser
import com.silwek.cleverchat.dummy.DummyContent
import com.silwek.cleverchat.models.ChatRoom

/**
 * @author Silwèk on 12/01/2018
 */
class FirebaseDatabase {
    fun getChatRoomsForUser(user: FirebaseUser, onResult: (List<ChatRoom>) -> Unit) {
        onResult(DummyContent.CHATROOMS)
    }
}