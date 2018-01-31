package com.silwek.cleverchat.presenters

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.silwek.cleverchat.databases.CoreDatabaseFactory
import com.silwek.cleverchat.getDatabaseFactory
import com.silwek.cleverchat.models.ChatUser
import com.silwek.cleverchat.viewmodels.ChatUserFriendsViewModel

/**
 * @author Silwèk on 31/01/2018
 */
class ChatFriendPresenter(private val dbFactory: CoreDatabaseFactory = getDatabaseFactory()) {

    fun getChatFriends(owner: LifecycleOwner, onFriends: (List<ChatUser>?) -> Unit) {
        val model = when (owner) {
            is FragmentActivity -> ViewModelProviders.of(owner).get(ChatUserFriendsViewModel::class.java)
            is Fragment -> ViewModelProviders.of(owner).get(ChatUserFriendsViewModel::class.java)
            else -> throw IllegalArgumentException("Only Fragment and FragmentActivity are supported")
        }
        model.getChatUserFriends()?.observe(owner, Observer<List<ChatUser>?> { friends -> onFriends(friends) })
    }
}