package com.example.travelapp2.data.network

import com.example.travelapp2.data.models.RouteFirebase
import com.example.travelapp2.data.models.UserPlaceCategoryPreferences

data class UserProfileRequest(
    val user_profile: UserPlaceCategoryPreferences,
    val lat: Double,
    val lon: Double,
    val user_lat: Double,
    val user_lon: Double,
    val num_routes: Int,
    val places_per_route: Int
    )

data class Place(
    val name: String,
    val address: String,
    val category: String,
    val lon: Double,
    val lat: Double,
    val opening_hours: String,
    val distance_to_next: Double,
    val duration_to_next: Double
)

data class Route(
    val route_number: Int,
    val places: List<Place>,
    val total_distance: Double,
    val total_visit_time: Double,
    val total_duration: Double
)


data class RouteRecommendationRequest(
    val user_preferences: RoutePreference,
    val routes: List<RouteFirebase>,
    val top_n: Int = 5
)


data class RoutePreference(
    val typesOfRoute: Map<String, Int>,
    val routeTime: Map<String, Int>,
    val difficultyLevel: Map<String, Int>,
    val typeOfTransport: Map<String, Int>,
    val budgetRange: Map<String, Int> = mapOf("start" to 0, "end" to 0)
)

data class BudgetRange(
    val start: Int,
    val end: Int
)


data class RecommendationResponse(
    val recommended_routes: List<RecommendedRoute>
)

data class RecommendedRoute(
    val id: String,
    val similarity: Float
)

data class RouteItem(
    val route_id: String,
    val name: String,
    val typesOfRoute: List<String>,
    val routeTime: String,
    val difficultyLevel: String,
    val typeOfTransport: String,
    val budgetRange: BudgetRange
)

fun RouteFirebase.toRouteItem(): RouteItem {
    return RouteItem(
        route_id = id,
        name = title,
        typesOfRoute = typesOfRoute,
        routeTime = routeTime,
        difficultyLevel = difficultyLevel,
        typeOfTransport = typeOfTransport,
        budgetRange = BudgetRange(
            start = budgetRange["start"] ?: 0,
            end = budgetRange["end"] ?: 0
        )
    )
}
