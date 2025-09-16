package com.example.travelapp2.data.models

import com.google.firebase.Timestamp

data class RouteFirebase(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val city: String = "",
    val description: String = "",
    val photos: List<String> = emptyList(),
    val typesOfRoute: List<String> = emptyList(),
    val routeTime: String = "",
    val exactDays: Int = 0,
    val difficultyLevel: String = "",
    val typeOfTransport: String = "",
    val budgetRange: Map<String, Int> = mapOf("start" to 0, "end" to 0),
    val route: List<Map<String, Any>> = emptyList(),
    val convenience: List<String> = emptyList(),
    val recommendations: String = "",
    val totalDuration: Double = .0,
    val totalDistance: Double = .0,
    val generatedByApp: Boolean = false,
    val viewCount: Int = 0,
    val saveCount: Int = 0,
    val dateOfCreation: Timestamp = Timestamp.now(),
    val public: Boolean = false
)