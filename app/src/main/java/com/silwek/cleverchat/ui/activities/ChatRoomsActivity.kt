package com.silwek.cleverchat.ui.activities

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import com.silwek.cleverchat.R
import com.silwek.cleverchat.models.ChatRoom
import com.silwek.cleverchat.ui.adapters.SimpleRecyclerViewAdapter
import com.silwek.cleverchat.ui.fragments.ChatFragment
import com.silwek.cleverchat.ui.fragments.CreateChatFragment
import com.silwek.cleverchat.viewmodels.ChatRoomsViewModel
import kotlinx.android.synthetic.main.activity_chatrooms.*
import kotlinx.android.synthetic.main.include_chatrooms.*
import kotlinx.android.synthetic.main.item_chat.view.*


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ChatActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 *
 * @author Silwèk on 12/01/2018
 */
class ChatRoomsActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    private lateinit var chatRoomsAdapter: SimpleItemRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatrooms)

        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener { view ->
            goToCreateChatActivity()
        }

        if (chatroom_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        setupRecyclerView(chatroom_list)
        val model = ViewModelProviders.of(this).get(ChatRoomsViewModel::class.java!!)
        model.getChatRooms()?.observe(this, Observer { rooms -> chatRoomsAdapter.values = rooms })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_account -> goToAccountActivity()
        }
        return true
    }

    private fun goToAccountActivity() {
        startActivity(Intent(this, AccountActivity::class.java))
    }

    private fun goToCreateChatActivity() {
        startActivityForResult(Intent(this, CreateChatActivity::class.java), REQUEST_CREATE_CHAT)
    }

    private fun showChat(chat: ChatRoom) {
        if (twoPane) {
            val fragment = ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ChatFragment.ARG_CHAT_ID, chat.id)
                    putString(ChatFragment.ARG_CHAT_NAME, chat.name)
                }
            }
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.chatroom_detail_container, fragment)
                    .commit()
        } else {
            val intent = Intent(this, ChatActivity::class.java).apply {
                putExtra(ChatFragment.ARG_CHAT_ID, chat.id)
                putExtra(ChatFragment.ARG_CHAT_NAME, chat.name)
            }
            startActivity(intent)
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        chatRoomsAdapter = SimpleItemRecyclerViewAdapter { showChat(it) }
        recyclerView.adapter = chatRoomsAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CREATE_CHAT && resultCode == Activity.RESULT_OK) {
            val chatId = data?.getStringExtra(CreateChatFragment.RESULT_CHAT_ID)
            val chatName = data?.getStringExtra(CreateChatFragment.RESULT_CHAT_NAME)
            val chat = ChatRoom(name = chatName, id = chatId)
            showChat(chat)
        }
    }

    class SimpleItemRecyclerViewAdapter(onItemClick: (ChatRoom) -> Unit) :
            SimpleRecyclerViewAdapter<ChatRoom, SimpleItemRecyclerViewAdapter.ViewHolder>(onItemClick) {
        override fun createView(parent: ViewGroup): SimpleItemRecyclerViewAdapter.ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat, parent, false)
            return ViewHolder(view)
        }

        override fun bind(holder: SimpleItemRecyclerViewAdapter.ViewHolder, item: ChatRoom, position: Int) {
            holder.mContentView.text = item.name
        }

        inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
            val mContentView: TextView = mView.content
        }
    }

    private val REQUEST_CREATE_CHAT = 2106
}
