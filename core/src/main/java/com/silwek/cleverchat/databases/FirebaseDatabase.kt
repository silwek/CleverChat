package com.silwek.cleverchat.databases

import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase
import com.silwek.cleverchat.databases.IChatDatabase.Companion.NODE_CHATS
import com.silwek.cleverchat.databases.IChatDatabase.Companion.NODE_MEMBERS
import com.silwek.cleverchat.databases.IChatDatabase.Companion.NODE_MESSAGES
import com.silwek.cleverchat.databases.IChatDatabase.Companion.NODE_ROOT
import com.silwek.cleverchat.databases.IChatDatabase.Companion.NODE_USERS
import com.silwek.cleverchat.getDatabaseFactory
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
class FirebaseDatabase(private val firebaseInstance: FirebaseDatabase = FirebaseDatabase.getInstance(), private val dbFactory: CoreDatabaseFactory = getDatabaseFactory()) : AnkoLogger, IChatDatabase {

    private val DATABASE: DatabaseReference by lazy {
        firebaseInstance.reference
    }

    private var messageQuery: Query? = null
    private var messageUpdateListener: ChildEventListener? = null

    override fun getDatabase(): DatabaseReference {
        return DATABASE
    }

    override fun getChatRoomsForUser(onResult: (List<ChatRoom>) -> Unit) {
        val userId = dbFactory.getUserDatabase()?.getCurrentUserId()
        when (userId) {
            null -> onResult(ArrayList<ChatRoom>(0))
            else -> loadChatRoomsForUser(userId, onResult)
        }
    }

    private fun loadChatRoomsForUser(userId: String, onResult: (List<ChatRoom>) -> Unit) {
        DATABASE.child(NODE_ROOT).child(NODE_USERS).child(userId).child(NODE_CHATS).addListenerForSingleValueEvent(object : ValueEventListener {
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
            DATABASE.child(NODE_ROOT).child(NODE_CHATS).child(it.key).addListenerForSingleValueEvent(object : ValueEventListener {
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

    override fun createChat(name: String, members: List<ChatUser>, onResult: (String) -> Unit) {
        if (name.isNotBlank() && members.isNotEmpty()) {
            //Create new chat
            val chatsNode = DATABASE.child(NODE_ROOT).child(NODE_CHATS)
            val chatNode = chatsNode.push()
            val chatId = chatNode.getKey()
            val chat = ChatRoom(name = name)
            chatNode.setValue(chat)

            //Init members
            val memberMaps = buildChatMembers(members)
            val membersNode = DATABASE.child(NODE_ROOT).child(NODE_MEMBERS).child(chatId)
            membersNode.setValue(memberMaps)

            //Update users
            members.forEach {
                if (it.id != null)
                    DATABASE.updateChildren(mapOf(Pair("/$NODE_ROOT/$NODE_USERS/${it.id}/$NODE_CHATS/$chatId", true)))
            }

            onResult(chatId)
        }
    }

    private fun buildChatMembers(members: List<ChatUser>): Map<String, Boolean> {
        val membersMap = HashMap<String, Boolean>(members.size)
        members.forEach {
            if (it.id != null)
                membersMap.put(it.id!!, true)
        }
        return membersMap
    }

    override fun sendMessage(chatId: String, content: String, onSuccess: () -> Unit) {
        if (chatId.isNotBlank() && content.isNotBlank()) {
            val author = dbFactory.getUserDatabase()?.getCurrentUser()
            author.notNull {
                val message = ChatMessage(content, it.id ?: "", it.name ?: "", Date().time)
                val messageNode = DATABASE.child(NODE_ROOT).child(NODE_MESSAGES).child(chatId).push()
                messageNode.setValue(message)
                onSuccess()
            }
        }
    }

    override fun getChatMessages(chatId: String,
                                 onMessageAdded: (ChatMessage, String?) -> Unit,
                                 onMessageRemoved: (ChatMessage, String?) -> Unit) {
        if (chatId.isNotBlank()) {
            messageQuery = DATABASE.child(NODE_ROOT).child(NODE_MESSAGES).child(chatId).orderByChild("date")
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

    }

    override fun stopGetChatMessages() {
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


}