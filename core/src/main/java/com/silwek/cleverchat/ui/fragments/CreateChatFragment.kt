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
import com.silwek.cleverchat.ui.adapters.SimpleRecyclerViewAdapter
import com.silwek.cleverchat.viewmodels.ChatUserFriendsViewModel
import kotlinx.android.synthetic.main.fragment_create_chat.*
import kotlinx.android.synthetic.main.item_chat_user.view.*

/**
 * @author Silwèk on 13/01/2018
 */
class CreateChatFragment : Fragment() {
    private lateinit var chatUserFriendsAdapter: SimpleItemRecyclerViewAdapter

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
        val model = ViewModelProviders.of(this).get(ChatUserFriendsViewModel::class.java!!)
        model.getChatUserFriends()?.observe(this, Observer { friends -> chatUserFriendsAdapter.values = friends })
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        chatUserFriendsAdapter = SimpleItemRecyclerViewAdapter { onFriendSelected(it) }
        recyclerView.adapter = chatUserFriendsAdapter
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

    class SimpleItemRecyclerViewAdapter(onItemClick: (ChatUser) -> Unit) :
            SimpleRecyclerViewAdapter<ChatUser, SimpleItemRecyclerViewAdapter.ViewHolder>(onItemClick) {
        var friendsList: MutableList<ChatUser> = ArrayList()

        fun switchFriend(user: ChatUser) {
            if (friendsList.contains(user))
                friendsList.remove(user)
            else
                friendsList.add(user)
            if (values != null)
                notifyItemChanged(values!!.indexOf(user))
        }

        override fun createView(parent: ViewGroup): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_user, parent, false)
            return ViewHolder(view)
        }

        override fun bind(holder: ViewHolder, item: ChatUser, position: Int) {
            holder.contentView.text = item.name
            holder.contentCheck.isChecked = friendsList.contains(item)
        }

        inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
            val contentView = mView.content
            val contentCheck = mView.contentCheck
        }
    }

    companion object {
        val RESULT_CHAT_ID = "RESULT_CHAT_ID"
        val RESULT_CHAT_NAME = "RESULT_CHAT_NAME"
    }
}
