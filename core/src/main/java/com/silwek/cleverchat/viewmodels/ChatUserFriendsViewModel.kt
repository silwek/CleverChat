package com.silwek.cleverchat.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.silwek.cleverchat.getDatabaseFactory
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
        getDatabaseFactory().getFriendsDatabase()?.getUsers({
            val userId = getDatabaseFactory().getUserDatabase()?.getCurrentUserId()
            chatUsers?.value = it.filter {
                it.id != userId
            }
        })
    }
}