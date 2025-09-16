package com.example.travelapp2.data.models

data class UserPlaceCategoryPreferences(
    val userId: String,
    val preferredPlaceCategory: Map<String, Double>,
)