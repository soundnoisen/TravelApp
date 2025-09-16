package com.example.travelapp2.data.repository

import android.util.Log
import com.example.travelapp2.data.models.RouteFirebase
import com.example.travelapp2.data.network.RetrofitInstance
import com.example.travelapp2.data.network.Route
import com.example.travelapp2.data.network.RoutePreference
import com.example.travelapp2.data.network.RouteRecommendationRequest
import com.example.travelapp2.data.network.UserProfileRequest
import com.example.travelapp2.domain.repository.RecommendationRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RecommendationRepositoryImpl(
    private val db: FirebaseFirestore
) : RecommendationRepository {

    private val routesCollection = db.collection("routes")

    override suspend fun getPublicRoutes(): List<RouteFirebase> = withContext(Dispatchers.IO) {
        try {
            val snapshot = routesCollection.whereEqualTo("public", true).get().await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(RouteFirebase::class.java)?.copy(id = document.id)
            }
        } catch (e: Exception) {
            Log.e("RecommendationRepo", "Ошибка получения публичных маршрутов: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getRouteRecommendations(
        userPreferences: RoutePreference,
        routes: List<RouteFirebase>
    ): List<RouteFirebase> = withContext(Dispatchers.IO) {
        try {
            val request = RouteRecommendationRequest(
                user_preferences = userPreferences,
                routes = routes,
                top_n = 5
            )
            val response = RetrofitInstance.apiService.getRouteRecommendations(request)
            if (response.isSuccessful) {
                val recommendedRoutes = response.body()?.recommended_routes ?: emptyList()
                val filteredRoutes = routes.filter { route ->
                    recommendedRoutes.any { it.id == route.id }
                }
                return@withContext filteredRoutes.sortedByDescending { route ->
                    recommendedRoutes.find { it.id == route.id }?.similarity ?: 0f
                }
            } else {
                Log.e("RecommendationRepo", "Ошибка сервера: ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("RecommendationRepo", "Ошибка запроса рекомендаций: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getGeneratedRoutes(request: UserProfileRequest): List<Route>? =
        withContext(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.apiService.getGeneratedRoutes(request)
                if (response.isSuccessful) response.body() else null
            } catch (e: Exception) {
                Log.e("RecommendationRepo", "Ошибка получения сгенерированных маршрутов: ${e.message}")
                null
            }
        }
}
