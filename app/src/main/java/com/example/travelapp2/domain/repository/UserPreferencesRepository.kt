package com.example.travelapp2.domain.repository

import com.example.travelapp2.data.models.RouteFirebase
import com.example.travelapp2.data.models.UserRoutesPreferences

interface UserPreferencesRepository {
    suspend fun getUserPreferences(userId: String): UserRoutesPreferences
    suspend fun updateUserRoutePreferencesOnSave(userId: String, route: RouteFirebase)
    suspend fun updateUserRoutePreferencesOnView(userId: String, route: RouteFirebase)
    suspend fun updatePlaceCategoryPreferenceOnSave(userId: String, category: String)
    suspend fun updatePlaceCategoryPreferenceOnView(userId: String, category: String)
}

