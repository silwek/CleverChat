package com.silwek.cleverchat.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.silwek.cleverchat.R
import com.silwek.cleverchat.models.ChatMessage
import kotlinx.android.synthetic.main.item_message.view.*

/**
 * @author Silwèk on 30/01/2018
 */
class ChatMessageViewAdapter(onItemClick: (ChatMessage) -> Unit) :
        SimpleRecyclerViewAdapter<ChatMessage, ChatMessageViewAdapter.ViewHolder>(onItemClick) {
    var userId: String? = null
    override fun createView(parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message, parent, false)
        return ViewHolder(view)
    }

    override fun bind(holder: ViewHolder, item: ChatMessage, position: Int) {
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