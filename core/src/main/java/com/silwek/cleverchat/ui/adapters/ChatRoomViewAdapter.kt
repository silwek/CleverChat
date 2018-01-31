package com.silwek.cleverchat.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.silwek.cleverchat.R
import com.silwek.cleverchat.models.ChatRoom
import kotlinx.android.synthetic.main.item_chat.view.*

/**
 * @author Silwèk on 30/01/2018
 */
class ChatRoomViewAdapter(onItemClick: (ChatRoom) -> Unit) :
        SimpleRecyclerViewAdapter<ChatRoom, ChatRoomViewAdapter.ViewHolder>(onItemClick) {
    override fun createView(parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat, parent, false)
        return ViewHolder(view)
    }

    override fun bind(holder: ViewHolder, item: ChatRoom, position: Int) {
        holder.mContentView.text = item.name
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val mContentView: TextView = mView.content
    }
}