package com.example.travelapp2.data.repository

import com.example.travelapp2.data.models.ChatMessage
import com.example.travelapp2.domain.repository.ChatRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ChatRepository {

    private var listener: ListenerRegistration? = null

    override fun listenToMessages(chatId: String, onMessagesChanged: (List<ChatMessage>) -> Unit) {
        listener?.remove()

        listener = firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, _ ->
                val messages = snapshot?.toObjects(ChatMessage::class.java) ?: emptyList()
                onMessagesChanged(messages)
            }
    }

    override suspend fun sendMessage(chatId: String, message: ChatMessage) {
        firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .add(message)
    }
}
