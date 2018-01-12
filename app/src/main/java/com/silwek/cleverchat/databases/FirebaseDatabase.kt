package com.silwek.cleverchat.databases

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase
import com.silwek.cleverchat.models.ChatRoom
import com.silwek.cleverchat.notNull
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug


/**
 * @author Silwèk on 12/01/2018
 */
class FirebaseDatabase : AnkoLogger {

    private val database: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().reference;
    }

    private fun getUserId(): String? {
        val user = FirebaseAuth.getInstance().currentUser
        return user?.uid
    }

    fun getChatRoomsForUser(onResult: (List<ChatRoom>) -> Unit) {
        val userId = getUserId()
        when (userId) {
            null -> ArrayList<ChatRoom>(0)
            else -> loadChatRoomsForUser(userId, onResult)
        }
    }

    private fun loadChatRoomsForUser(userId: String, onResult: (List<ChatRoom>) -> Unit) {
        database.child(NODE_ROOT).child(NODE_USERS).child(userId).child(NODE_CHATS).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                debug { "Count " + dataSnapshot?.getChildrenCount() }
                val rooms = ArrayList<ChatRoom>()
                when (dataSnapshot) {
                    null -> rooms
                    else ->
                        for (chatSnapshot in dataSnapshot.children) {
                            getChatRoom(chatSnapshot, rooms, onResult)
                        }
                }
            }
        })
    }

    private fun getChatRoom(dataSnapshot: DataSnapshot?, rooms: MutableList<ChatRoom>, onResult: (List<ChatRoom>) -> Unit) {
        dataSnapshot.notNull {
            database.child(NODE_ROOT).child(NODE_CHATS).child(it.key).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    dataSnapshot.notNull {
                        if (it.childrenCount > 0) {
                            it.getValue(ChatRoom::class.java).notNull {
                                rooms.add(it)
                                onResult(rooms)
                            }
                        }
                    }
                }
            })
        }
    }

    companion object {
        val NODE_ROOT = "chat"
        val NODE_CHATS = "chats"
        val NODE_MEMBERS = "members"
        val NODE_USERS = "users"
    }
}