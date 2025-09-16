package com.example.travelapp2.data.models

data class UserProfileFirebase(
    val userId: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val email: String = "",
    val bio: String = "",
    val socialLinks: List<Map<String, String>> = emptyList(),
    val subscribersId: List<String> = emptyList(),
    val subscriptionsId: List<String> = emptyList(),
    val savedRoutesId: List<String> = emptyList(),
    val savedPlacesId: List<String> = emptyList(),
)