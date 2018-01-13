package com.silwek.cleverchat.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.silwek.cleverchat.databases.DatabaseFactory
import com.silwek.cleverchat.models.ChatUser


/**
 * @author Silwèk on 13/01/2018
 */
class ChatUserFriendsViewModel() : ViewModel() {
    private var chatUsers: MutableLiveData<List<ChatUser>>? = null
    fun getChatUserFriends(): LiveData<List<ChatUser>>? {
        if (chatUsers == null) {
            chatUsers = MutableLiveData<List<ChatUser>>()
            loadUserFriends()
        }
        return chatUsers
    }

    private fun loadUserFriends() {
        DatabaseFactory.firebaseDatabase.getUsers({
            val userId = DatabaseFactory.firebaseDatabase.getCurrentUserId()
            chatUsers?.value = it.filter {
                it.id != userId
            }
        })
    }
}