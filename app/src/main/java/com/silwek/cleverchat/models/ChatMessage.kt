package com.silwek.cleverchat.models

/**
 * @author Silwèk on 12/01/2018
 */
data class ChatMessage(
        var message: String? = null, var authorId: String? = null, var authorName: String? = null, var id: String? = null)