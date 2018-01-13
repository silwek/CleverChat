package com.silwek.cleverchat.models

/**
 * @author Silwèk on 12/01/2018
 */
data class ChatMessage(
        var message: String, var authorId: String, var authorName: String, val date: Long, var id: String? = null)