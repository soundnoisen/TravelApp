package com.example.travelapp2.presentation.profile.createRoute

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp2.data.models.RouteFirebase
import com.example.travelapp2.data.models.RoutePoint
import com.example.travelapp2.domain.repository.RouteRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RouteCreationViewModel @Inject constructor(
    private val routeRepository: RouteRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val user = auth.currentUser

    private val _routeData = MutableLiveData<RouteFirebase>()
    val routeData: LiveData<RouteFirebase> get() = _routeData

    fun setRoutePoints(points: List<Map<String, Any>>) {
        updateRouteData {
            copy(route = points)
        }
    }

    fun updateIsPublic(isPublic: Boolean) {
        _routeData.value = _routeData.value?.copy(public = isPublic)
    }

    private fun updateRouteData(update: RouteFirebase.() -> RouteFirebase) {
        val currentData = _routeData.value ?: RouteFirebase()
        _routeData.value = currentData.update()
    }

    fun saveRouteToFirebase(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val route = _routeData.value ?: return

        user?.let { currentUser ->
            val routeWithUser = route.copy(userId = currentUser.uid)
            viewModelScope.launch {
                try {
                    val tripId = routeRepository.saveRoute(routeWithUser)
                    Log.d("RouteCreationVM", "Маршрут успешно добавлен: $tripId")
                    onSuccess()
                    clearRouteData()
                } catch (e: Exception) {
                    Log.w("RouteCreationVM", "Ошибка при добавлении маршрута", e)
                    onFailure(e)
                }
            }
        }
    }

    fun saveGenRouteToFirebase(route: RouteFirebase, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        user?.let { currentUser ->
            val routeWithUser = route.copy(userId = currentUser.uid)
            viewModelScope.launch {
                try {
                    val tripId = routeRepository.saveRoute(routeWithUser)
                    Log.d("RouteCreationVM", "Маршрут успешно добавлен: $tripId")
                    onSuccess()
                    clearRouteData()
                } catch (e: Exception) {
                    Log.w("RouteCreationVM", "Ошибка при добавлении маршрута", e)
                    onFailure(e)
                }
            }
        }
    }

    private fun clearRouteData() {
        _routeData.value = RouteFirebase()
    }

    fun setRoute(route: RouteFirebase) {
        _routeData.value = route
    }


    fun updateCity(city: String) {
        _routeData.value = _routeData.value?.copy(city = city)
    }

    fun updateTitle(title: String) {
        _routeData.value = _routeData.value?.copy(title = title)
    }

    fun updateDescription(description: String) {
        _routeData.value = _routeData.value?.copy(description = description)
    }

    fun updateRecommendations(recommendations: String) {
        _routeData.value = _routeData.value?.copy(recommendations = recommendations)
    }

    fun updatePhotosUrls(photosUrls: List<String>) {
        _routeData.value = _routeData.value?.copy(photos = photosUrls)
    }

    fun updateTime(time: String) {
        _routeData.value = _routeData.value?.copy(routeTime = time)
    }

    fun updateDays(number: Int) {
        _routeData.value = _routeData.value?.copy(exactDays = number)
    }

    fun updateDifficulty(difficulty: String) {
        _routeData.value = _routeData.value?.copy(difficultyLevel = difficulty)
    }

    fun updatePrice(startPrice: Int, endPrice: Int) {
        _routeData.value = _routeData.value?.copy(budgetRange = mapOf("start" to startPrice, "end" to endPrice))
    }

    fun updateTransport(typeOfTransport: String) {
        _routeData.value = _routeData.value?.copy(typeOfTransport = typeOfTransport)
    }

    fun updateConveniences(conveniences: List<String>) {
        _routeData.value = _routeData.value?.copy(convenience = conveniences)
    }

    fun updateTypes(types: List<String>) {
        _routeData.value = _routeData.value?.copy(typesOfRoute = types)
    }


}
