package com.silwek.cleverchat.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.silwek.cleverchat.R
import com.silwek.cleverchat.getToolbarManager
import com.silwek.cleverchat.models.ChatRoom
import com.silwek.cleverchat.presenters.ChatPresenter
import com.silwek.cleverchat.ui.adapters.ChatMessageViewAdapter
import kotlinx.android.synthetic.main.view_chat.*
import kotlinx.android.synthetic.main.view_chat.view.*

/**
 * @author Silwèk on 12/01/2018
 */
class ChatFragment() : Fragment() {

    companion object {
        const val ARG_CHAT_ID = "chat_id"
        const val ARG_CHAT_NAME = "chat_name"
    }

    private var chat: ChatRoom? = null
    private var messagesAdapter: ChatMessageViewAdapter? = null
    private val chatPresenter: ChatPresenter by lazy { ChatPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_CHAT_ID)) {
                chat = ChatRoom(id = it.getString(ARG_CHAT_ID), name = it.getString(ARG_CHAT_NAME))
                getToolbarManager()?.setActionBarTitle(chat?.name ?: "")
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.view_chat, container, false)

        setupRecyclerView(rootView.messagesList)
        initData()

        rootView.btSend.setOnClickListener {
            sendMessage()
        }
        return rootView
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        messagesAdapter = ChatMessageViewAdapter({

        })
        recyclerView.adapter = messagesAdapter
        messagesAdapter?.userId = chatPresenter.getUserId()
        getToolbarManager()?.attachToScroll(recyclerView)
    }

    private fun initData() {
        val chatId = chat?.id
        chatId?.let {
            chatPresenter.getChatMessages(chatId, this) { messages -> messagesAdapter?.values = messages }
        }
    }

    private fun sendMessage() {
        val message = fieldMessage.text.toString()
        val chatId = chat?.id
        chatId?.let {
            chatPresenter.sendMessage(chatId, message) { fieldMessage.setText("") }
        }
    }
}
