package com.silwek.cleverchat.ui.fragments

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.*
import com.silwek.cleverchat.R
import com.silwek.cleverchat.databases.CoreDatabaseFactory
import com.silwek.cleverchat.getDatabaseFactory
import com.silwek.cleverchat.models.ChatUser
import com.silwek.cleverchat.ui.adapters.FriendViewAdapter
import com.silwek.cleverchat.viewmodels.ChatUserFriendsViewModel
import kotlinx.android.synthetic.main.fragment_create_chat.*

/**
 * @author Silwèk on 13/01/2018
 */
class CreateChatFragment : Fragment() {
    companion object {
        const val RESULT_CHAT_ID = "RESULT_CHAT_ID"
        const val RESULT_CHAT_NAME = "RESULT_CHAT_NAME"
    }

    private lateinit var chatUserFriendsAdapter: FriendViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_chat, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.create_chat_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_done -> createChat()
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        setupRecyclerView(membersList)
        initDataObserver()
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        chatUserFriendsAdapter = FriendViewAdapter { onFriendSelected(it) }
        recyclerView.adapter = chatUserFriendsAdapter
    }

    private fun initDataObserver() {
        val model = ViewModelProviders.of(this).get(ChatUserFriendsViewModel::class.java)
        model.getChatUserFriends()?.observe(this, Observer { friends -> chatUserFriendsAdapter.values = friends })
    }

    private fun onFriendSelected(friend: ChatUser) {
        chatUserFriendsAdapter.switchFriend(friend)
    }

    private fun createChat() {
        if (fieldName != null && chatUserFriendsAdapter.friendsList.isNotEmpty()) {
            val name = fieldName.text.toString();
            val user = getDatabaseFactory().getUserDatabase()?.getCurrentUser()
            val members = chatUserFriendsAdapter.friendsList.toMutableList()
            members.add(ChatUser(user?.name, user?.id))
            CoreDatabaseFactory.firebaseDatabase.createChat(name, members, {
                val i = Intent().apply {
                    putExtra(RESULT_CHAT_ID, it)
                    putExtra(RESULT_CHAT_NAME, name)
                }
                activity?.setResult(Activity.RESULT_OK, i)
                activity?.finish()
            })
        } else {
            TODO("Show fields error")
        }
    }
}
