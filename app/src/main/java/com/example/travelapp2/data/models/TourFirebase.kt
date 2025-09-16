package com.example.travelapp2.data.models


data class TourFirebase(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val city: String = "",
    val public: Boolean = false,
    val startDate: String = "",
    val endDate: String = "",
    val photoUrl: String = "",
    val adminId: String = "",
    val participants: List<String> = emptyList(),
    val requests: List<String> = emptyList(),
    val maxParticipants: Int = 2,
    val savedPlacesId: Map<String, List<String>> = emptyMap(),
    val savedRoutesId: List<String> = emptyList(),
    val route: List<Map<String, Any>> = emptyList(),
    val chatId: String = "",
)

data class ChatFirebase(
    val id: String = "",
    val tourId: String = "",
    val participants: List<String> = emptyList(),
)

data class ChatMessage(
    val id: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val avatarUrl: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
