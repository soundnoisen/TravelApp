package com.example.travelapp2.presentation.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp2.data.models.LocationPointFirebase
import com.example.travelapp2.domain.repository.LocationPointRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationPointFirebaseViewModel @Inject constructor(
    private val locationPointRepository: LocationPointRepository
) : ViewModel() {

    private val _locations = MutableLiveData<List<LocationPointFirebase>>()
    val locations: LiveData<List<LocationPointFirebase>> get() = _locations

    private val _selectedImages = MutableStateFlow<List<Map<String, List<Uri>>>>(emptyList())
    val selectedImages: StateFlow<List<Map<String, List<Uri>>>> get() = _selectedImages

    fun updateImagesById(id: String, newImages: List<Uri>) {
        val currentImages = _selectedImages.value.toMutableList()
        val index = currentImages.indexOfFirst { it.containsKey(id) }
        if (index != -1) {
            currentImages[index] = mapOf(id to newImages)
        } else {
            currentImages.add(mapOf(id to newImages))
        }
        _selectedImages.value = currentImages
    }

    fun getImagesById(id: String): List<Uri> {
        return _selectedImages.value.find { it.containsKey(id) }?.get(id) ?: emptyList()
    }

    fun getLocationsByTripId(tripId: String) {
        viewModelScope.launch {
            val locationsList = locationPointRepository.getLocationsByTripId(tripId)
            _locations.value = locationsList
            Log.d("LocationPointVM", "Загружены точки путешествия: ${locationsList.size}")
        }
    }

    fun addLocationToTrip(
        tripId: String,
        location: LocationPointFirebase,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (locationPointRepository.addLocationToTrip(tripId, location)) {
                    onSuccess()
                } else {
                    onFailure(Exception("Ошибка при добавлении точки"))
                }
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun updateLocations(newLocations: List<LocationPointFirebase>) {
        _locations.value = newLocations.sortedBy { it.index }
    }

    fun updateLocation(updatedLocation: LocationPointFirebase) {
        val updatedList = _locations.value?.map {
            if (it.id == updatedLocation.id) updatedLocation else it
        } ?: emptyList()
        _locations.value = updatedList
    }

    fun clearLocationPoint() {
        _locations.value = emptyList()
        _selectedImages.value = emptyList()
    }

    fun setLocations(locations: List<LocationPointFirebase>) {
        _locations.value = locations.sortedBy { it.index }
    }
}
