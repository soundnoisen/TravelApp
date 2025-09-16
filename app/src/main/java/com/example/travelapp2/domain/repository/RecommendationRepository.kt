package com.example.travelapp2.domain.repository

import com.example.travelapp2.data.models.RouteFirebase
import com.example.travelapp2.data.network.Route
import com.example.travelapp2.data.network.RoutePreference
import com.example.travelapp2.data.network.UserProfileRequest

interface RecommendationRepository {
    suspend fun getPublicRoutes(): List<RouteFirebase>
    suspend fun getRouteRecommendations(
        userPreferences: RoutePreference,
        routes: List<RouteFirebase>
    ): List<RouteFirebase>
    suspend fun getGeneratedRoutes(request: UserProfileRequest): List<Route>?
}
