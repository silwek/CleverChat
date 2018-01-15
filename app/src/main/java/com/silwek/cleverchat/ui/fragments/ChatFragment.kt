package com.silwek.cleverchat.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.silwek.cleverchat.R
import com.silwek.cleverchat.databases.DatabaseFactory
import com.silwek.cleverchat.getCompatActivity
import com.silwek.cleverchat.models.ChatMessage
import com.silwek.cleverchat.models.ChatRoom
import com.silwek.cleverchat.notNull
import com.silwek.cleverchat.setActionBarTitle
import com.silwek.cleverchat.ui.adapters.SimpleRecyclerViewAdapter
import com.silwek.cleverchat.viewmodels.ChatMessagesViewModel
import kotlinx.android.synthetic.main.item_message.view.*
import kotlinx.android.synthetic.main.view_chat.*
import kotlinx.android.synthetic.main.view_chat.view.*

/**
 * A fragment representing a single ChatRoom detail screen.
 * This fragment is either contained in a [ChatRoomsActivity]
 * in two-pane mode (on tablets) or a [ChatActivity]
 * on handsets.
 *
 * @author Silwèk on 12/01/2018
 */
class ChatFragment : Fragment() {

    private var chat: ChatRoom? = null
    private var messagesAdapter: SimpleItemRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_CHAT_ID)) {
                chat = ChatRoom(id = it.getString(ARG_CHAT_ID))
                if (it.containsKey(ARG_CHAT_NAME)) {
                    chat?.name = it.getString(ARG_CHAT_NAME)
                }
                getCompatActivity()?.setActionBarTitle(chat?.name ?: "")
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.view_chat, container, false)

        setupRecyclerView(rootView.messagesList)

        val model = ViewModelProviders.of(this).get(ChatMessagesViewModel::class.java!!)
        model.chatId = chat?.id
        model.getChatMessages()?.observe(this, Observer { messages -> messagesAdapter?.values = messages })

        rootView.btSend.setOnClickListener {
            val message = fieldMessage.text.toString()
            val chatId = chat?.id
            chatId.notNull {
                DatabaseFactory.firebaseDatabase.sendMessage(it, message, { fieldMessage.setText("") })
            }
        }

        return rootView
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        messagesAdapter = SimpleItemRecyclerViewAdapter({

        })
        recyclerView.adapter = messagesAdapter
        messagesAdapter?.userId = DatabaseFactory.firebaseDatabase.getCurrentUserId()
    }

    class SimpleItemRecyclerViewAdapter(onItemClick: (ChatMessage) -> Unit) :
            SimpleRecyclerViewAdapter<ChatMessage, SimpleItemRecyclerViewAdapter.ViewHolder>(onItemClick) {
        var userId: String? = null
        override fun createView(parent: ViewGroup): SimpleItemRecyclerViewAdapter.ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message, parent, false)
            return ViewHolder(view)
        }

        override fun bind(holder: SimpleItemRecyclerViewAdapter.ViewHolder, item: ChatMessage, position: Int) {
            holder.contentView.text = item.message
            if (item.authorId == userId)
                holder.container.gravity = Gravity.END or Gravity.RIGHT
            else
                holder.container.gravity = Gravity.START or Gravity.LEFT
        }

        inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
            val contentView: TextView = mView.message
            val container = mView.messageContainer
        }
    }

    companion object {
        /**
         * The fragment argument representing the chat ID that this fragment
         * represents.
         */
        const val ARG_CHAT_ID = "chat_id"
        /**
         * The fragment argument representing the chat name that this fragment
         * represents.
         */
        const val ARG_CHAT_NAME = "chat_name"
    }
}
