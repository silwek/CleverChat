package com.silwek.cleverchat.databases

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.nhaarman.mockito_kotlin.*
import com.silwek.cleverchat.models.ChatRoom
import com.silwek.cleverchat.models.ChatUser
import com.silwek.cleverchat.models.User
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.verification.VerificationMode
import com.nhaarman.mockito_kotlin.any as kotlinAny
import org.mockito.ArgumentMatchers as am


/**
 * @author Silwèk on 22/01/2018
 */
class FirebaseDatabaseTest {
    private lateinit var mFirebaseInstance: com.google.firebase.database.FirebaseDatabase
    private lateinit var mDatabaseFactory: CoreDatabaseFactory
    private lateinit var mFirebaseDatabaseReference: DatabaseReference
    private lateinit var mUserDatabase: IUserDatabase

    interface FunctionalObjOnChatRoomsResult {
        fun onResult(rooms: List<ChatRoom>)
    }

    interface FunctionalObjOnChatCreateResult {
        fun onResult(chatId: String)
    }

    interface FunctionalObjSendMessageSuccess {
        fun onSuccess()
    }

    private fun once(): VerificationMode {
        return times(1)
    }

    @Before
    fun setUp() {
        mFirebaseInstance = mock(com.google.firebase.database.FirebaseDatabase::class.java)
        mFirebaseDatabaseReference = mock(DatabaseReference::class.java)
        Mockito.`when`(mFirebaseDatabaseReference.child(am.anyString())).thenReturn(mFirebaseDatabaseReference)
        Mockito.`when`(mFirebaseDatabaseReference.push()).thenReturn(mFirebaseDatabaseReference)
        Mockito.`when`(mFirebaseInstance.reference).thenReturn(mFirebaseDatabaseReference)
        mDatabaseFactory = mock(CoreDatabaseFactory::class.java)
        mUserDatabase = mock(IUserDatabase::class.java)
        Mockito.`when`(mDatabaseFactory.getUserDatabase()).thenReturn(mUserDatabase)
    }

    @Test
    fun getDatabase_singletonInstance() {
        val fd = FirebaseDatabase(mFirebaseInstance, mDatabaseFactory)
        val db1 = fd.getDatabase()
        val db2 = fd.getDatabase()
        assertEquals(db1 === db2, true)
    }

    @Test
    fun getChatRoomsForUser_noUserNoRooms() {
        val fd = FirebaseDatabase(mFirebaseInstance, mDatabaseFactory)
        Mockito.`when`(mUserDatabase.getCurrentUserId()).thenReturn(null)
        val mockResult = mock(FunctionalObjOnChatRoomsResult::class.java)
        val noRooms = ArrayList<ChatRoom>(0)
        fd.getChatRoomsForUser(mockResult::onResult)
        verify(mockResult).onResult(eq(noRooms))
    }

    @Test
    fun getChatRoomsForUser_userAskForRooms() {
        val fd = FirebaseDatabase(mFirebaseInstance, mDatabaseFactory)
        Mockito.`when`(mUserDatabase.getCurrentUserId()).thenReturn("a-user-id")
        fd.getChatRoomsForUser {}
        verify(mFirebaseDatabaseReference, atLeastOnce()).child("a-user-id")
    }

    @Test
    fun createChat_noChatIfNoMember() {
        val fd = FirebaseDatabase(mFirebaseInstance, mDatabaseFactory)
        val noMembers = listOf<ChatUser>()
        val mockResult = mock(FunctionalObjOnChatCreateResult::class.java)
        Mockito.`when`(mFirebaseDatabaseReference.key).thenReturn("chat-key")
        fd.createChat("A chat", noMembers, mockResult::onResult)
        verify(mockResult, never()).onResult(kotlinAny())
    }

    @Test
    fun createChat_noChatIfEmptyName() {
        val fd = FirebaseDatabase(mFirebaseInstance, mDatabaseFactory)
        val members = listOf(ChatUser("User 1", "id-user-1"), ChatUser("User 2", "id-user-2"))
        val mockResult = mock(FunctionalObjOnChatCreateResult::class.java)
        Mockito.`when`(mFirebaseDatabaseReference.key).thenReturn("chat-key")
        fd.createChat("", members, mockResult::onResult)
        verify(mockResult, never()).onResult(kotlinAny())
    }

    @Test
    fun createChat_noChatIfEmptyNameAndNoMember() {
        val fd = FirebaseDatabase(mFirebaseInstance, mDatabaseFactory)
        val noMembers = listOf<ChatUser>()
        val mockResult = mock(FunctionalObjOnChatCreateResult::class.java)
        Mockito.`when`(mFirebaseDatabaseReference.key).thenReturn("chat-key")
        fd.createChat("", noMembers, mockResult::onResult)
        verify(mockResult, never()).onResult(kotlinAny())
    }

    @Test
    fun createChat_returnChatKey() {
        val fd = FirebaseDatabase(mFirebaseInstance, mDatabaseFactory)
        val members = listOf(ChatUser("User 1", "id-user-1"), ChatUser("User 2", "id-user-2"))
        val mockResult = mock(FunctionalObjOnChatCreateResult::class.java)
        Mockito.`when`(mFirebaseDatabaseReference.key).thenReturn("chat-key")
        fd.createChat("A chat", members, mockResult::onResult)
        verify(mockResult, once()).onResult(eq("chat-key"))
    }

    @Test
    fun createChat_insertAChat() {
        val fd = FirebaseDatabase(mFirebaseInstance, mDatabaseFactory)
        val members = listOf(ChatUser("User 1", "id-user-1"), ChatUser("User 2", "id-user-2"))
        Mockito.`when`(mFirebaseDatabaseReference.key).thenReturn("chat-key")
        fd.createChat("A chat", members) {}
        verify(mFirebaseDatabaseReference, atLeastOnce()).push()
        verify(mFirebaseDatabaseReference).setValue(am.any(ChatRoom::class.java))
    }

    @Test
    fun createChat_insertMembers() {
        val fd = FirebaseDatabase(mFirebaseInstance, mDatabaseFactory)
        val members = listOf(ChatUser("User 1", "id-user-1"), ChatUser("User 2", "id-user-2"))
        Mockito.`when`(mFirebaseDatabaseReference.key).thenReturn("chat-key")
        fd.createChat("A chat", members) {}
        val captor = ArgumentCaptor.forClass(Map::class.java)
        verify(mFirebaseDatabaseReference, atLeastOnce()).setValue(captor.capture())
        assertEquals(2, captor.value.size)
    }

    @Test
    fun sendMessage_noUserNoSuccess() {
        val fd = FirebaseDatabase(mFirebaseInstance, mDatabaseFactory)
        Mockito.`when`(mUserDatabase.getCurrentUser()).thenReturn(null)
        val mockResult = mock(FunctionalObjSendMessageSuccess::class.java)
        fd.sendMessage("a-chat-id", "A message", mockResult::onSuccess)
        verify(mockResult, never()).onSuccess()
    }

    @Test
    fun sendMessage_AUserAnEmptyChatIdNoSuccess() {
        val fd = FirebaseDatabase(mFirebaseInstance, mDatabaseFactory)
        Mockito.`when`(mUserDatabase.getCurrentUser()).thenReturn(User("A user", "a-user-id", Any()))
        val mockResult = mock(FunctionalObjSendMessageSuccess::class.java)
        fd.sendMessage("", "A message", mockResult::onSuccess)
        verify(mockResult, never()).onSuccess()
    }

    @Test
    fun sendMessage_AUserAnEmptyMessageNoSuccess() {
        val fd = FirebaseDatabase(mFirebaseInstance, mDatabaseFactory)
        Mockito.`when`(mUserDatabase.getCurrentUser()).thenReturn(User("A user", "a-user-id", Any()))
        val mockResult = mock(FunctionalObjSendMessageSuccess::class.java)
        fd.sendMessage("a-chat-id", "", mockResult::onSuccess)
        verify(mockResult, never()).onSuccess()
    }

    @Test
    fun sendMessage_AUserAnEmptyMessageAnEmptyChatIdNoSuccess() {
        val fd = FirebaseDatabase(mFirebaseInstance, mDatabaseFactory)
        Mockito.`when`(mUserDatabase.getCurrentUser()).thenReturn(User("A user", "a-user-id", Any()))
        val mockResult = mock(FunctionalObjSendMessageSuccess::class.java)
        fd.sendMessage("", "", mockResult::onSuccess)
        verify(mockResult, never()).onSuccess()
    }

    @Test
    fun sendMessage_AUserAMessageASuccess() {
        val fd = FirebaseDatabase(mFirebaseInstance, mDatabaseFactory)
        Mockito.`when`(mUserDatabase.getCurrentUser()).thenReturn(User("A user", "a-user-id", Any()))
        val mockResult = mock(FunctionalObjSendMessageSuccess::class.java)
        fd.sendMessage("a-chat-id", "A message", mockResult::onSuccess)
        verify(mockResult, once()).onSuccess()
    }

    @Test
    fun getChatMessages_emptyChatIdNoListener() {
        val fd = FirebaseDatabase(mFirebaseInstance, mDatabaseFactory)
        val query = mock(Query::class.java)
        Mockito.`when`(mFirebaseDatabaseReference.orderByChild(any())).thenReturn(query)
        fd.getChatMessages("", onMessageAdded = { _, _ -> }, onMessageRemoved = { _, _ -> })
        verify(query, never()).addChildEventListener(any())
    }

    @Test
    fun getChatMessages_addListener() {
        val fd = FirebaseDatabase(mFirebaseInstance, mDatabaseFactory)
        val query = mock(Query::class.java)
        Mockito.`when`(mFirebaseDatabaseReference.orderByChild(any())).thenReturn(query)
        fd.getChatMessages("a-chat-id", onMessageAdded = { _, _ -> }, onMessageRemoved = { _, _ -> })
        verify(query, once()).addChildEventListener(any())
    }

    @Test
    fun stopGetChatMessages_noListenerNoRemoveListener() {
        val fd = FirebaseDatabase(mFirebaseInstance, mDatabaseFactory)
        val query = mock(Query::class.java)
        Mockito.`when`(mFirebaseDatabaseReference.orderByChild(any())).thenReturn(query)
        fd.stopGetChatMessages()
        verify(query, never()).removeEventListener(am.any(ChildEventListener::class.java))
    }

    @Test
    fun stopGetChatMessages_removeListener() {
        val fd = FirebaseDatabase(mFirebaseInstance, mDatabaseFactory)
        val query = mock(Query::class.java)
        Mockito.`when`(mFirebaseDatabaseReference.orderByChild(any())).thenReturn(query)
        fd.getChatMessages("a-chat-id", onMessageAdded = { _, _ -> }, onMessageRemoved = { _, _ -> })
        fd.stopGetChatMessages()
        verify(query, once()).removeEventListener(am.any(ChildEventListener::class.java))
    }
}