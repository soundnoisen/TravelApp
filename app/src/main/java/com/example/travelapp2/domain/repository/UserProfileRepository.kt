package com.example.travelapp2.domain.repository

import com.example.travelapp2.data.models.PlaceFirebase
import com.example.travelapp2.data.models.UserProfileFirebase

interface UserProfileRepository {
    suspend fun getUserProfile(userId: String): UserProfileFirebase?
    suspend fun saveUserProfile(profile: UserProfileFirebase)
    suspend fun updateUserProfile(userId: String, updates: Map<String, Any>)
    suspend fun updateSavedRoutes(userId: String, newSavedRoutes: List<String>)
    suspend fun updateSavedPlaces(userId: String, savedPlacesId: List<String>)
    suspend fun removeSavedRoute(userId: String, routeId: String)
    suspend fun removeSavedPlace(userId: String, placeId: String)
    suspend fun toggleSavedRoute(userId: String, routeId: String): Boolean
    suspend fun toggleSavedPlace(userId: String, place: PlaceFirebase): Boolean
    suspend fun toggleNowSavedPlace(userId: String, place: PlaceFirebase): Boolean
    suspend fun getUsersByIds(userIds: List<String>, onComplete: (List<UserProfileFirebase>) -> Unit)
    suspend fun updateFieldUserProfile(profile: UserProfileFirebase)
    suspend fun deleteUserCompletely(userId: String): Boolean
    fun listenToUserProfile(userId: String, onProfileChanged: (UserProfileFirebase) -> Unit)
    suspend fun getUsersByIds(ids: List<String>): List<UserProfileFirebase>
    suspend fun removeSubscription(currentUserId: String, targetUserId: String)
    suspend fun removeSubscriber(currentUserId: String, subscriberId: String)
    suspend fun addSubscription(currentUserId: String, targetUserId: String)
}