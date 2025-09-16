package com.example.travelapp2.data.models

data class LocationPointFirebase(
    val id: String = "",
    val name: String = "",
    val city: String = "",
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val photos: List<String> = emptyList(),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String = "",
    val index: Int = 0
)