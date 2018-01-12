package com.silwek.cleverchat.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.silwek.cleverchat.dummy.DummyContent
import com.silwek.cleverchat.models.ChatRoom


/**
 * @author Silwèk on 12/01/2018
 */
class ChatRoomsViewModel() : ViewModel() {
    private var chatRooms: MutableLiveData<List<ChatRoom>>? = null
    fun getChatRooms(): LiveData<List<ChatRoom>>? {
        if (chatRooms == null) {
            chatRooms = MutableLiveData<List<ChatRoom>>()
            loadUsers()
        }
        return chatRooms
    }

    private fun loadUsers() {
        chatRooms?.value = DummyContent.CHATROOMS
    }
}