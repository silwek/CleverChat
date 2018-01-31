package com.silwek.cleverchat.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.silwek.cleverchat.R
import com.silwek.cleverchat.addFragment
import com.silwek.cleverchat.fragmentFor
import com.silwek.cleverchat.ui.fragments.ChatFragment
import org.jetbrains.anko.find

/**
 * An activity representing a single ChatRoom screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of ChatRooms
 * in a [ChatRoomsActivity].
 *
 * @author Silwèk on 12/01/2018
 */
class ChatActivity : AppCompatActivity(), ToolbarManager {
    override val toolbar: Toolbar by lazy { find<Toolbar>(R.id.toolbar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        setSupportActionBar(toolbar)

        // Show the Up button in the action bar.
        enableHomeAsUp { navigateUpTo(Intent(this, ChatRoomsActivity::class.java)) }

        if (savedInstanceState == null) {
            with(intent) {
                val fragment = fragmentFor<ChatFragment>(
                        ChatFragment.ARG_CHAT_ID to getStringExtra(ChatFragment.ARG_CHAT_ID),
                        ChatFragment.ARG_CHAT_NAME to getStringExtra(ChatFragment.ARG_CHAT_NAME))
                addFragment(fragment, R.id.chatroom_detail_container)
            }
        }
    }
}
