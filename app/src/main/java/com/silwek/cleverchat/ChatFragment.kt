package com.silwek.cleverchat

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.silwek.cleverchat.dummy.DummyContent
import kotlinx.android.synthetic.main.item_message.view.*
import kotlinx.android.synthetic.main.view_chat.view.*

/**
 * A fragment representing a single ChatRoom detail screen.
 * This fragment is either contained in a [ChatRoomsActivity]
 * in two-pane mode (on tablets) or a [ChatActivity]
 * on handsets.
 */
class ChatFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private var mChat: DummyContent.DummyChat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_CHAT_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                mChat = DummyContent.CHATROOMS_MAP[it.getString(ARG_CHAT_ID)]
                if (mChat != null) {
                    getCompatActivity()?.setActionBarTitle(mChat!!.title)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.view_chat, container, false)

        if (mChat != null)
            setupRecyclerView(rootView.messages_list, mChat!!.messages)

        return rootView
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, messages: List<DummyContent.DummyMessage>) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(messages)
    }

    class SimpleItemRecyclerViewAdapter(private val mValues: List<DummyContent.DummyMessage>) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = mValues[position]
            holder.mContentView.text = item.content

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
    }
}
