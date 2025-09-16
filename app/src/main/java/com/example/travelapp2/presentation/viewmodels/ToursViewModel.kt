package com.example.travelapp2.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp2.data.models.PlaceFirebase
import com.example.travelapp2.data.models.TourFirebase
import com.example.travelapp2.data.models.UserProfileFirebase
import com.example.travelapp2.domain.repository.PlacesRepository
import com.example.travelapp2.domain.repository.RouteRepository
import com.example.travelapp2.domain.repository.ToursRepository
import com.example.travelapp2.domain.repository.UserProfileRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ToursViewModel @Inject constructor(
    private val toursRepository: ToursRepository,
    private val auth: FirebaseAuth,
    private val userProfileRepository: UserProfileRepository,
    private val placesRepository: PlacesRepository
) : ViewModel() {

    private val _publicTours = MutableLiveData<List<TourFirebase>>()
    val publicTours: LiveData<List<TourFirebase>> = _publicTours

    private val _userTours = MutableLiveData<List<TourFirebase>>()
    val userTours: LiveData<List<TourFirebase>> = _userTours

    private val _foundTour = MutableLiveData<TourFirebase?>()
    val foundTour: LiveData<TourFirebase?> = _foundTour

    private val _participantAvatars = MutableLiveData<List<Pair<String, String>>>()
    val participantAvatars: LiveData<List<Pair<String, String>>> get() = _participantAvatars

    private val _placesByCategory = MutableLiveData<Map<String, List<PlaceFirebase>>>()
    val placesByCategory: LiveData<Map<String, List<PlaceFirebase>>> = _placesByCategory


    private val userId: String = auth.currentUser?.uid ?: throw IllegalStateException("Пользователь не авторизован")

    init {
        listenToUserTours()
        listenToPublicTours()
    }

    private fun listenToPublicTours() {
        toursRepository.listenToPublicTours { tours ->
            _publicTours.value = tours
        }
    }

    private fun listenToUserTours() {
        toursRepository.listenToUserTours(userId) { tours ->
            _userTours.value = tours
        }
    }

    fun listenToTourById(tourId: String) {
        toursRepository.listenToTourChanges(tourId) { tour ->
            _foundTour.value = tour
            tour?.let { loadPlacesForTour(it.savedPlacesId) }

        }
    }


    fun listenToParticipants(tourId: String) {
        toursRepository.listenToParticipants(tourId) { participants ->
            loadParticipantAvatars(participants)
        }
    }

    fun loadParticipantAvatars(userIds: List<String>) {
        viewModelScope.launch {
            val avatars = toursRepository.fetchParticipantsAvatars(userIds)
            _participantAvatars.postValue(avatars)
        }
    }


    fun loadPlacesForTour(savedPlacesMap: Map<String, List<String>>) {
        viewModelScope.launch {
            val allPlaces = mutableMapOf<String, List<PlaceFirebase>>()

            savedPlacesMap.forEach { (category, ids) ->
                val places = placesRepository.fetchPlacesByIds(ids)
                allPlaces[category] = places
            }

            _placesByCategory.postValue(allPlaces)
        }
    }


    fun updateSavedPlaces(tourId: String, updatedSavedPlaces: Map<String, List<String>>) {
        viewModelScope.launch {
            try {
                toursRepository.updateSavedPlaces(tourId, updatedSavedPlaces)
            } catch (e: Exception) {
            }
        }
    }

    fun loadPlacesByIds(ids: List<String>, onResult: (List<PlaceFirebase>) -> Unit) {
        viewModelScope.launch {
            try {
                val places = placesRepository.fetchPlacesByIds(ids)
                onResult(places)
            } catch (e: Exception) {
                Log.e("ToursViewModel", "Ошибка при загрузке мест: ${e.message}")
                onResult(emptyList())
            }
        }
    }

    fun addPlacesToCategory(
        tourId: String,
        categoryKey: String,
        placeIds: Set<String>
    ) {
        viewModelScope.launch {
            val tour = toursRepository.getTourById(tourId)
            tour?.let {
                val currentPlaces = it.savedPlacesId[categoryKey]?.toMutableList() ?: mutableListOf()
                currentPlaces.addAll(placeIds)
                val updated = currentPlaces.distinct()
                toursRepository.updatePlacesByCategory(tourId, categoryKey, updated)
            }
        }
    }


    fun addPlacesToRouteFromCategory(
        categoryKey: String,
        placeIds: Set<String>,
        placesByCategory: Map<String, List<PlaceFirebase>>,
        onSuccess: () -> Unit = {}
    ) {
        val tour = _foundTour.value ?: return

        val selectedPlaces = placesByCategory[categoryKey]?.filter { placeIds.contains(it.id) } ?: emptyList()

        val newRoutePoints = selectedPlaces.map { place ->
            mapOf(
                "latitude" to (place.latitude),
                "longitude" to (place.longitude),
                "name" to (place.name)
            )
        }

        val updatedRoute = tour.route + newRoutePoints

        updateRouteForTour(tour.id, updatedRoute)
        onSuccess()
    }


    fun updateRouteForTour(tourId: String, newRoute: List<Map<String, Any>>) {
        viewModelScope.launch {
            toursRepository.updateTourRoute(tourId, newRoute)
        }
    }


    fun canEditTour(tour: TourFirebase, currentUserId: String): Boolean {
        return tour.participants.contains(currentUserId)
    }


    fun requestToJoinTour(tourId: String, userId: String) {
        viewModelScope.launch {
            try {
                toursRepository.requestToJoinTour(tourId, userId)
            } catch (e: Exception) {
                Log.e("ToursViewModel", "Ошибка при подаче заявки: ${e.message}")
            }
        }
    }




    fun approveJoinRequest(tourId: String, userId: String) {
        viewModelScope.launch {
            try {
                toursRepository.approveJoinRequest(tourId, userId)
            } catch (e: Exception) {
                Log.e("ToursViewModel", "Ошибка при принятии заявки", e)
            }
        }
    }

    fun rejectJoinRequest(tourId: String, userId: String) {
        viewModelScope.launch {
            try {
                toursRepository.rejectJoinRequest(tourId, userId)
            } catch (e: Exception) {
                Log.e("ToursViewModel", "Ошибка при отклонении заявки", e)
            }
        }
    }


    fun removeParticipant(tourId: String, userId: String) {
        viewModelScope.launch {
            try {
                toursRepository.removeParticipant(tourId, userId)
            } catch (e: Exception) {
                Log.e("ToursViewModel", "Ошибка при удалении участника", e)
            }
        }
    }

    fun loadUsersByIds(userIds: List<String>, onComplete: (List<UserProfileFirebase>) -> Unit) {
        viewModelScope.launch {
            try {
                userProfileRepository.getUsersByIds(userIds, onComplete)
            } catch (e: Exception) {
                Log.e("ToursViewModel", "Ошибка при удалении участника", e)
            }
        }
    }

    fun updateTour(tourId: String, updatedTour: TourFirebase) {
        viewModelScope.launch {
            try {
                toursRepository.updateTour(tourId, updatedTour)
            } catch (e: Exception) {
                Log.e("ToursViewModel", "Error updating tour", e)
            }
        }
    }

    fun deleteTour(tourId: String) {
        viewModelScope.launch {
            try {
                toursRepository.deleteTour(tourId)
            } catch (e: Exception) {
                Log.e("ToursViewModel", "Error updating tour", e)
            }
        }
    }

}
