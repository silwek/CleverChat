package com.silwek.cleverchat.databases

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase
import com.silwek.cleverchat.models.ChatMessage
import com.silwek.cleverchat.models.ChatRoom
import com.silwek.cleverchat.models.ChatUser
import com.silwek.cleverchat.notNull
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import java.util.*


/**
 * @author Silwèk on 12/01/2018
 */
class FirebaseDatabase : AnkoLogger {

    private val database: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().reference;
    }
    private var messageQuery: Query? = null
    private var messageUpdateListener: ChildEventListener? = null

    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    fun getCurrentUserId(): String? {
        val user = FirebaseAuth.getInstance().currentUser
        return user?.uid
    }

    fun getChatRoomsForUser(onResult: (List<ChatRoom>) -> Unit) {
        val userId = getCurrentUserId()
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
                                it.id = dataSnapshot?.key
                                rooms.add(it)
                                onResult(rooms)
                            }
                        }
                    }
                }
            })
        }
    }

    fun getUsers(onResult: (List<ChatUser>) -> Unit) {
        database.child(NODE_ROOT).child(NODE_USERS).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                val users = ArrayList<ChatUser>()
                dataSnapshot.notNull {
                    for (userSnapshot in it.children) {
                        val user = userSnapshot.getValue(ChatUser::class.java)
                        user?.id = userSnapshot.key
                        user?.let {
                            users.add(user)
                        }
                    }
                }
                onResult(users)
            }

        })
    }

    fun createChat(name: String, members: List<ChatUser>, onResult: (String) -> Unit) {
        //Create new chat
        val chatsNode = database.child(NODE_ROOT).child(NODE_CHATS)
        val chatNode = chatsNode.push()
        val chatId = chatNode.getKey()
        val chat = ChatRoom(name = name)
        chatNode.setValue(chat)

        //Init members
        val memberMaps = buildChatMembers(members)
        val membersNode = database.child(NODE_ROOT).child(NODE_MEMBERS).child(chatId)
        membersNode.setValue(memberMaps)

        //Update users
        members.forEach {
            if (it.id != null)
                database.updateChildren(mapOf(Pair("/$NODE_ROOT/$NODE_USERS/${it.id}/$NODE_CHATS/$chatId", true)))
        }

        onResult(chatId)
    }

    private fun buildChatMembers(members: List<ChatUser>): Map<String, Boolean> {
        val membersMap = HashMap<String, Boolean>(members.size)
        members.forEach {
            if (it.id != null)
                membersMap.put(it.id!!, true)
        }
        return membersMap
    }

    fun sendMessage(chatId: String, content: String, onSuccess: () -> Unit) {
        val author = getCurrentUser()
        author.notNull {
            val message = ChatMessage(content, it.uid, it.displayName ?: "", Date().time)
            val messageNode = database.child(NODE_ROOT).child(NODE_MESSAGES).child(chatId).push()
            messageNode.setValue(message)
            onSuccess()
        }
    }


    fun getChatMessages(chatId: String,
                        onMessageAdded: (ChatMessage, String?) -> Unit,
                        onMessageRemoved: (ChatMessage, String?) -> Unit) {
        messageQuery = database.child(NODE_ROOT).child(NODE_MESSAGES).child(chatId).orderByChild("date")
        messageUpdateListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                if (dataSnapshot != null)
                    convertToMessage(dataSnapshot, dataSnapshot.key).notNull {
                        onMessageAdded(it, previousChildName)
                    }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot?, previousChildName: String?) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
                if (dataSnapshot != null)
                    convertToMessage(dataSnapshot, dataSnapshot.key).notNull {
                        onMessageRemoved(it, dataSnapshot.key)
                    }
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot?, previousChildName: String?) {

            }

            override fun onCancelled(databaseError: DatabaseError?) {

            }
        }
        messageQuery?.addChildEventListener(messageUpdateListener)

    }

    fun stopGetChatMessages() {
        if (messageQuery != null && messageUpdateListener != null) {
            messageQuery?.removeEventListener(messageUpdateListener);
            messageUpdateListener = null;
        }
    }

    private fun convertToMessage(messageSnapshot: DataSnapshot, key: String): ChatMessage? {
        val message = messageSnapshot.getValue(ChatMessage::class.java)
        message?.id = key
        return message
    }

    companion object {
        val NODE_ROOT = "chat"
        val NODE_CHATS = "chats"
        val NODE_MEMBERS = "members"
        val NODE_USERS = "users"
        val NODE_MESSAGES = "messages"
    }

}