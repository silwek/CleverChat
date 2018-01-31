package com.silwek.cleverchat.presenters

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.silwek.cleverchat.databases.CoreDatabaseFactory
import com.silwek.cleverchat.getDatabaseFactory
import com.silwek.cleverchat.models.ChatRoom
import com.silwek.cleverchat.viewmodels.ChatRoomsViewModel

/**
 * @author Silwèk on 31/01/2018
 */
class ChatRoomsPresenter(private val dbFactory: CoreDatabaseFactory = getDatabaseFactory()) {

    init {
        dbFactory.getFriendsDatabase()?.insertChatUserIfNeeded()
    }

    fun getChatRooms(owner: LifecycleOwner, onRooms: (List<ChatRoom>?) -> Unit) {
        val model = when (owner) {
            is FragmentActivity -> ViewModelProviders.of(owner).get(ChatRoomsViewModel::class.java)
            is Fragment -> ViewModelProviders.of(owner).get(ChatRoomsViewModel::class.java)
            else -> throw IllegalArgumentException("Only Fragment and FragmentActivity are supported")
        }
        model.getChatRooms()?.observe(owner, Observer<List<ChatRoom>?> { rooms -> onRooms(rooms) })
    }
}