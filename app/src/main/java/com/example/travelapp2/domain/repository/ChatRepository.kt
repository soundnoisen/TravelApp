package com.example.travelapp2.domain.repository

import com.example.travelapp2.data.models.ChatMessage

interface ChatRepository {
    fun listenToMessages(chatId: String, onMessagesChanged: (List<ChatMessage>) -> Unit)
    suspend fun sendMessage(chatId: String, message: ChatMessage)
}
