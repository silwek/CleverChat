package com.silwek.cleverchat.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.silwek.cleverchat.R
import com.silwek.cleverchat.dummy.DummyContent
import com.silwek.cleverchat.getCompatActivity
import com.silwek.cleverchat.models.ChatMessage
import com.silwek.cleverchat.models.ChatRoom
import com.silwek.cleverchat.setActionBarTitle
import kotlinx.android.synthetic.main.item_message.view.*
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

    private var mChat: ChatRoom? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_CHAT_ID)) {
                mChat = ChatRoom(id = it.getString(ARG_CHAT_ID))
                if (it.containsKey(ARG_CHAT_NAME)) {
                    mChat?.name = it.getString(ARG_CHAT_NAME)
                }
                getCompatActivity()?.setActionBarTitle(mChat?.name ?: "")
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.view_chat, container, false)

        setupRecyclerView(rootView.messages_list, DummyContent.CHATMESSAGES)

        return rootView
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, messages: List<ChatMessage>) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(messages)
    }

    class SimpleItemRecyclerViewAdapter(private val mValues: List<ChatMessage>) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = mValues[position]
            holder.mContentView.text = item.message

            with(holder.itemView) {
                tag = item
            }
        }

        override fun getItemCount(): Int {
            return mValues.size
        }

        inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
            val mContentView: TextView = mView.message
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
