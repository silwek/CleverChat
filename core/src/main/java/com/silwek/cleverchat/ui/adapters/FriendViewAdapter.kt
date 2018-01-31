package com.silwek.cleverchat.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.silwek.cleverchat.R
import com.silwek.cleverchat.models.ChatUser
import kotlinx.android.synthetic.main.item_chat_user.view.*

/**
 * @author Silwèk on 31/01/2018
 */
class FriendViewAdapter(onItemClick: (ChatUser) -> Unit) :
        SimpleRecyclerViewAdapter<ChatUser, FriendViewAdapter.ViewHolder>(onItemClick) {
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