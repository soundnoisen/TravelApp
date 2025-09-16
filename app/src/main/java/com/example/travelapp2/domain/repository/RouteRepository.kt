package com.example.travelapp2.domain.repository

import com.example.travelapp2.data.models.RouteFirebase

interface RouteRepository {
    suspend fun getRoutesByUserId(userId: String): List<RouteFirebase>
    suspend fun getInterestingRoutes(): List<RouteFirebase>
    suspend fun getSavedRoutes(userId: String): List<RouteFirebase>
    suspend fun getRouteById(routeId: String): RouteFirebase?
    suspend fun deleteRoute(routeId: String)
    suspend fun saveRoute(route: RouteFirebase): String
    suspend fun getPublicRoutesUser(userId: String): List<RouteFirebase>
    suspend fun incrementSaveCount(routeId: String)
    suspend fun incrementViewCount(routeId: String)
}
