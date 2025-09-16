package com.example.travelapp2.data.models

data class PlaceFirebase(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val photoUrl: String = "",
    val addressComment: String = "",
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    val mainPhotoUrl: String = "",
    val schedule: Map<String, List<Map<String, String>>>? = emptyMap(),
    val is24x7: Boolean = false,
    val comment: String = "",
    val phone: String = "",
    val website: String = "",
    val description: String = "",
    val categories: List<String> = emptyList(),
    val openingHours: String = "",
    val fromApi: String = "",
)
