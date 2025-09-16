package com.example.travelapp2.data.models

import com.google.firebase.Timestamp

data class TripFirebase(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val city: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val description: String = "",
    val photos: List<String> = emptyList(),
    val dateOfCreation: Timestamp = Timestamp.now(),
    val isPublic: Boolean = false
)