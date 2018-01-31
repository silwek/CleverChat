package com.silwek.cleverchat.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.silwek.cleverchat.*
import com.silwek.cleverchat.models.ChatRoom
import com.silwek.cleverchat.presenters.ChatRoomsPresenter
import com.silwek.cleverchat.ui.adapters.ChatRoomViewAdapter
import com.silwek.cleverchat.ui.fragments.ChatFragment
import com.silwek.cleverchat.ui.fragments.CreateChatFragment
import kotlinx.android.synthetic.main.activity_chatrooms.*
import kotlinx.android.synthetic.main.include_chatrooms.*
import org.jetbrains.anko.find


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
class ChatRoomsActivity : AppCompatActivity(), ToolbarManager {
    companion object {
        private const val REQUEST_CREATE_CHAT = 2106
    }

    override val toolbar: Toolbar by lazy { find<Toolbar>(R.id.toolbar) }

    private var twoPane: Boolean = false
    private lateinit var chatRoomsAdapter: ChatRoomViewAdapter
    private val chatRoomsPresenter: ChatRoomsPresenter by lazy { ChatRoomsPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatrooms)
        setSupportActionBar(toolbar)
        setActionBarTitle(getString(R.string.app_name))

        twoPane = (chatroom_detail_container != null)
        setupRecyclerView(chatroom_list)
        initData()
        fab.setOnClickListener { _ -> goToCreateChatActivity() }
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

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        chatRoomsAdapter = ChatRoomViewAdapter { showChat(it) }
        recyclerView.adapter = chatRoomsAdapter
    }

    private fun initData() {
        chatRoomsPresenter.getChatRooms(this) { rooms -> chatRoomsAdapter.values = rooms }
    }

    private fun goToAccountActivity() {
        startActivity(CoreApplication.instance.getAccountIntent())
    }

    private fun goToCreateChatActivity() {
        startActivityForResult(Intent(this, CreateChatActivity::class.java), Companion.REQUEST_CREATE_CHAT)
    }

    private fun showChat(chat: ChatRoom) {
        with(chat) {
            val chatParams: Map<String, Any?> = mapOf(
                    ChatFragment.ARG_CHAT_ID to id,
                    ChatFragment.ARG_CHAT_NAME to name)
            if (twoPane) {
                val fragment = fragmentFor<ChatFragment>(chatParams)
                replaceFragment(fragment, R.id.chatroom_detail_container)
            } else {
                startActivity<ChatActivity>(chatParams)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Companion.REQUEST_CREATE_CHAT && resultCode == Activity.RESULT_OK) {
            data?.let {
                val chatId = it.getStringExtra(CreateChatFragment.RESULT_CHAT_ID)
                val chatName = it.getStringExtra(CreateChatFragment.RESULT_CHAT_NAME)
                val chat = ChatRoom(name = chatName, id = chatId)
                showChat(chat)
            }

        }
    }
}
