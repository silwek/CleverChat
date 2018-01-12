package com.silwek.cleverchat.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import com.silwek.cleverchat.R
import com.silwek.cleverchat.dummy.DummyContent
import com.silwek.cleverchat.ui.fragments.ChatFragment
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
 */
class ChatRoomsActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var mTwoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatrooms)

        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener { view ->
            Snackbar.make(view, "New chat is coming", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        if (chatroom_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true
        }

        setupRecyclerView(chatroom_list)
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

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, DummyContent.CHATROOMS, mTwoPane)
    }

    class SimpleItemRecyclerViewAdapter(private val mParentActivity: ChatRoomsActivity,
                                        private val mValues: List<DummyContent.DummyChat>,
                                        private val mTwoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val mOnClickListener: View.OnClickListener

        init {
            mOnClickListener = View.OnClickListener { v ->
                val chat = v.tag as DummyContent.DummyChat
                if (mTwoPane) {
                    val fragment = ChatFragment().apply {
                        arguments = Bundle().apply {
                            putString(ChatFragment.ARG_CHAT_ID, chat.id)
                        }
                    }
                    mParentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.chatroom_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, ChatActivity::class.java).apply {
                        putExtra(ChatFragment.ARG_CHAT_ID, chat.id)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = mValues[position]
            holder.mContentView.text = item.title

            with(holder.itemView) {
                tag = item
                setOnClickListener(mOnClickListener)
            }
        }

        override fun getItemCount(): Int {
            return mValues.size
        }

        inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
            val mContentView: TextView = mView.content
        }
    }
}
