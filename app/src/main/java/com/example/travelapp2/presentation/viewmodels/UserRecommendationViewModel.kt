package com.example.travelapp2.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp2.data.models.RouteFirebase
import com.example.travelapp2.data.models.UserPlaceCategoryPreferences
import com.example.travelapp2.data.network.Place
import com.example.travelapp2.data.network.RetrofitInstance
import com.example.travelapp2.data.network.Route
import com.example.travelapp2.data.network.RoutePreference
import com.example.travelapp2.data.network.UserProfileRequest
import com.example.travelapp2.domain.repository.RecommendationRepository
import com.example.travelapp2.domain.repository.UserPreferencesRepository
import com.example.travelapp2.main.home.filters.RouteFilter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

@HiltViewModel
class UserRecommendationViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val recommendationRepository: RecommendationRepository,
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth) : ViewModel() {

    private val userId: String = auth.currentUser?.uid!!
    private val userRef = db.collection("usersPreferences").document(userId)

    private val _recommendations = MutableLiveData<List<Place>>()
    val recommendations: LiveData<List<Place>> get() = _recommendations

    private val _routes = MutableLiveData<List<Route>>()
    val routes: LiveData<List<Route>> get() = _routes

    private val _allRoutes = MutableLiveData<List<RouteFirebase>>()

    private val _routesRecommendation = MutableLiveData<List<RouteFirebase>>()
    val routesRecommendation:LiveData<List<RouteFirebase>> get() = _routesRecommendation

    var useCurrentLocation by mutableStateOf(true)
    var numberOfRoutes by mutableIntStateOf(3)
    var placesPerRoute by mutableIntStateOf(5)
    var region by mutableStateOf<GeoPoint?>(null)


    val start = MutableStateFlow(false)
    val isLoading = MutableStateFlow(false)


    init {
        onInitUserRecommendation()
    }


    fun clearRoutes() {
        _routes.value = emptyList()
    }

    fun getGeneratedRoutes(lat: Double, lon: Double, userLat: Double, userLon: Double) {

        viewModelScope.launch(Dispatchers.IO) {
            val profilePrefs = userPreferencesRepository.getUserPreferences(userId)

            val userPlaceCategoryPreferencesRequest = UserProfileRequest(
                user_profile = UserPlaceCategoryPreferences(userId, profilePrefs.preferredPlaceCategory),
                lat = lat,
                lon = lon,
                user_lat = userLat,
                user_lon = userLon,
                num_routes = numberOfRoutes,
                places_per_route = placesPerRoute,
            )

            try {
                val response = RetrofitInstance.apiService.getGeneratedRoutes(userPlaceCategoryPreferencesRequest)
                if (response.isSuccessful) {
                    val routesList = response.body()
                    routesList?.let {
                        _routes.postValue(it)
                        Log.i("UserRecommendationVM", "Успешно получены маршруты: ${it.size}")
                    }
                } else {
                    Log.e("UserRecommendationVM", "Ошибка запроса: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("UserRecommendationVM", "Ошибка при получении маршрутов: ${e.message}")
            }
        }
    }

    private fun getRouteRecommendations(userPreferences: RoutePreference, routes: List<RouteFirebase>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recommendedRoutes = recommendationRepository.getRouteRecommendations(userPreferences, routes)
                _routesRecommendation.postValue(recommendedRoutes)
                Log.i("UserRecommendationVM", "Рекомендовано маршрутов: ${recommendedRoutes.size}")
            } catch (e: Exception) {
                Log.e("UserRecommendationVM", "Ошибка запроса: ${e.message}")
            }
        }
    }

    private fun onInitUserRecommendation() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val publicRoutes = recommendationRepository.getPublicRoutes()
                _allRoutes.postValue(publicRoutes)
                Log.w("UserRecommendationVM", "Получены публичные маршруты, их: ${publicRoutes.size}")

                userRef.get().addOnSuccessListener { document ->
                    if (document != null) {
                        val preferredDifficultyLevels = document.get("preferredDifficultyLevels") as? Map<String, Long>
                        val preferredRouteTimes = document.get("preferredRouteTimes") as? Map<String, Long>
                        val preferredTransportTypes = document.get("preferredTransportTypes") as? Map<String, Long>
                        val preferredTypesOfRoute = document.get("preferredTypesOfRoute") as? Map<String, Long>

                        val userPreferences = RoutePreference(
                            typesOfRoute = preferredTypesOfRoute?.mapValues { it.value.toInt() } ?: emptyMap(),
                            routeTime = preferredRouteTimes?.mapValues { it.value.toInt() } ?: emptyMap(),
                            difficultyLevel = preferredDifficultyLevels?.mapValues { it.value.toInt() } ?: emptyMap(),
                            typeOfTransport = preferredTransportTypes?.mapValues { it.value.toInt() } ?: emptyMap()
                        )

                        _allRoutes.value?.let { getRouteRecommendations(userPreferences, it) }
                    }
                }.addOnFailureListener { exception ->
                    Log.w("UserRecommendationVM", "Ошибка получения пользовательских предпочтений", exception)
                }
            } catch (e: Exception) {
                Log.e("UserRecommendationVM", "Ошибка при загрузке публичных маршрутов: ${e.message}")
            }
        }
    }
}
