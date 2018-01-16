package com.silwek.cleverchat.databases

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.silwek.cleverchat.databases.IChatDatabase.Companion.NODE_ROOT
import com.silwek.cleverchat.databases.IChatDatabase.Companion.NODE_USERS
import com.silwek.cleverchat.getDatabaseFactory
import com.silwek.cleverchat.models.ChatUser
import com.silwek.cleverchat.notNull

/**
 * @author Silwèk on 15/01/2018
 */
class FriendsDatabase : IFriendsDatabase {
    override fun insertChatUserIfNeeded() {
        val user = getDatabaseFactory().getUserDatabase()?.getCurrentUser()
        user?.let {
            val chatUserId = user.id;
            val chatUser = ChatUser(user.name)
            val database = getDatabaseFactory().getChatDatabase()?.getDatabase()

            database?.let {
                database.child(NODE_ROOT).child(NODE_USERS).child(chatUserId).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        if (dataSnapshot?.value == null) {
                            database.updateChildren(mapOf(Pair("/$NODE_ROOT/$NODE_USERS/${chatUserId}", chatUser)))
                        }
                    }
                })

            }
        }

    }

    override fun getUsers(onResult: (List<ChatUser>) -> Unit) {
        val database = getDatabaseFactory().getChatDatabase()?.getDatabase()

        database?.let {
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

    }
}