package com.example.travelapp2.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp2.data.models.RouteFirebase
import com.example.travelapp2.data.network.Place
import com.example.travelapp2.data.network.Route
import com.example.travelapp2.domain.repository.RouteRepository
import com.example.travelapp2.emptyTourPhoto
import com.example.travelapp2.main.home.filters.RouteFilter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RouteFirebaseViewModel @Inject constructor(
    private val routeRepository: RouteRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _interestingRoutes = MutableLiveData<List<RouteFirebase>>()
    val interestingRoutes: LiveData<List<RouteFirebase>> get() = _interestingRoutes

    private val _savedRoutes = MutableLiveData<List<RouteFirebase>>()
    val savedRoutes: LiveData<List<RouteFirebase>> get() = _savedRoutes

    private val _userRoutes = MutableLiveData<List<RouteFirebase>>()
    val userRoutes: LiveData<List<RouteFirebase>> get() = _userRoutes

    private val _publicRoutesUser = MutableLiveData<List<RouteFirebase>>()
    val publicRoutesUser: LiveData<List<RouteFirebase>> get() = _publicRoutesUser

    private val _routeGeneratedByApp = MutableLiveData<RouteFirebase>()
    val routeGeneratedByApp: LiveData<RouteFirebase> get() = _routeGeneratedByApp

    private val _routePlacesGeneratedByApp = MutableLiveData<List<Place>>()
    val routePlacesGeneratedByApp: LiveData<List<Place>> get() = _routePlacesGeneratedByApp


    private val _filteredInteresting = MutableLiveData<List<RouteFirebase>>()
    val filteredInteresting: LiveData<List<RouteFirebase>> = _filteredInteresting

    private val _filteredSaved = MutableLiveData<List<RouteFirebase>>()
    val filteredSaved: LiveData<List<RouteFirebase>> = _filteredSaved


    fun getRoutesByUserId(userId: String) {
        viewModelScope.launch {
            try {
                val routes = routeRepository.getRoutesByUserId(userId)
                _userRoutes.postValue(routes)
                Log.d("Firestore", "Загружены маршруты пользователя: ${routes.size}")
            } catch (e: Exception) {
                Log.w("Firestore", "Ошибка при загрузке маршрутов пользователя", e)
            }
        }
    }


    fun getPublicRoutesUser(userId: String) {
        viewModelScope.launch {
            try {
                val routes = routeRepository.getPublicRoutesUser(userId)
                _publicRoutesUser.postValue(routes)
                Log.d("Firestore", "Загружены публичные маршруты: ${routes.size}")
            } catch (e: Exception) {
                Log.w("Firestore", "Ошибка при загрузке публичных маршрутов", e)
            }
        }
    }


    fun routeConversion(city: String, route: Route): RouteFirebase {
        return RouteFirebase(
            title = "Сгенерированный маршрут",
            city = city,
            photos = listOf(emptyTourPhoto),
            routeTime = route.total_visit_time.toString(),
            route = route.places.map { point ->
                mapOf(
                    "latitude" to point.lat,
                    "longitude" to point.lon,
                    "name" to point.name,
                    "city" to city,
                    "address" to point.address
                )
            },
            totalDistance = route.total_distance,
            totalDuration = route.total_duration,
            generatedByApp = true
        )
    }

    fun updateRouteGeneratedByApp(city: String, route: Route) {
        _routeGeneratedByApp.value = routeConversion(city, route)
    }

    fun updatePlacesGeneratedByApp(places: List<Place>) {
        _routePlacesGeneratedByApp.value = places
    }


    fun loadInterestingRoutes() {
        viewModelScope.launch {
            val top = routeRepository.getInterestingRoutes()
            _interestingRoutes.value = top
        }
    }

    fun applyFilterToInteresting(filter: RouteFilter) {
        val raw = _interestingRoutes.value.orEmpty()
        _filteredInteresting.value = raw.filter { it.matches(filter) }
    }

    fun loadSavedRoutes() {
        viewModelScope.launch {
            auth.currentUser?.uid?.let { uid ->
                val data = routeRepository.getSavedRoutes(uid)
                _savedRoutes.value = data
            }
        }
    }

    fun applyFilterToSaved(filter: RouteFilter) {
        val raw = _savedRoutes.value.orEmpty()
        _filteredSaved.value = raw.filter { it.matches(filter) }
    }


    private fun RouteFirebase.matches(filter: RouteFilter): Boolean {
        return (filter.city.isEmpty() || city.equals(filter.city, true)) &&
                (filter.routeTime.isEmpty() || routeTime == filter.routeTime) &&
                (filter.exactDays.isEmpty() || exactDays == filter.exactDays.toIntOrNull()) &&
                (filter.difficulty.isEmpty() || difficultyLevel == filter.difficulty) &&
                (filter.transportType.isEmpty() || typeOfTransport == filter.transportType) &&
                (filter.budgetStart.isEmpty() || (budgetRange["start"]
                    ?: 0) >= (filter.budgetStart.toIntOrNull() ?: 0)) &&
                (filter.budgetEnd.isEmpty() || (budgetRange["end"]
                    ?: 0) <= (filter.budgetEnd.toIntOrNull() ?: Int.MAX_VALUE)) &&
                (filter.routeTypes.isEmpty() || filter.routeTypes.any { it in typesOfRoute })
    }


    fun getRouteById(routeId: String): LiveData<RouteFirebase?> {
        val liveData = MutableLiveData<RouteFirebase?>()
        viewModelScope.launch {
            try {
                val route = routeRepository.getRouteById(routeId)
                liveData.postValue(route)
            } catch (e: Exception) {
                Log.w("Firestore", "Ошибка при загрузке маршрута по ID", e)
            }
        }
        return liveData
    }


    fun deleteRoute(routeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                routeRepository.deleteRoute(routeId)
            } catch (e: Exception) {
                Log.w("RouteFirebaseViewModel", "Ошибка при удалении маршрута: ${e.message}")
            }
        }
    }


    fun onRouteViewed(route: RouteFirebase) {
        viewModelScope.launch(Dispatchers.IO) {
            routeRepository.incrementViewCount(route.id)
        }
    }

    fun onRouteSaved(route: RouteFirebase) {
        viewModelScope.launch(Dispatchers.IO) {
            routeRepository.incrementSaveCount(route.id)
        }
    }

}