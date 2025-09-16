package com.example.travelapp2.presentation.profile.createTrip

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp2.data.models.LocationPointFirebase
import com.example.travelapp2.data.models.RouteFirebase
import com.example.travelapp2.data.models.TripFirebase
import com.example.travelapp2.domain.repository.TripRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TripCreationViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    private val auth: FirebaseAuth,
) : ViewModel() {

    private val user = auth.currentUser

    private val _tripData = MutableLiveData(TripFirebase())
    val tripData: LiveData<TripFirebase> get() = _tripData

    private val _locations = MutableLiveData<List<LocationPointFirebase>>(emptyList())
    val locations: LiveData<List<LocationPointFirebase>> get() = _locations

    private val _selectedImages = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImages: StateFlow<List<Uri>> get() = _selectedImages

    fun updateSelectedImages(newImages: List<Uri>) {
        _selectedImages.value = newImages
    }

    fun updateLocations(locations: List<LocationPointFirebase>) {
        _locations.value = locations
    }

    fun updateTitle(title: String) {
        _tripData.value = _tripData.value?.copy(title = title)
    }

    fun updateCity(city: String) {
        _tripData.value = _tripData.value?.copy(city = city)
    }

    fun updateDates(startDate: String, endDate: String) {
        _tripData.value = _tripData.value?.copy(startDate = startDate, endDate = endDate)
    }

    fun updateDescription(description: String) {
        _tripData.value = _tripData.value?.copy(description = description)
    }

    fun updatePhotosUrls(photos: List<String>) {
        _tripData.value = _tripData.value?.copy(photos = photos)
    }

    fun updateIsPublic(isPublic: Boolean) {
        _tripData.value = _tripData.value?.copy(isPublic = isPublic)
    }

    fun addLocation(location: LocationPointFirebase) {
        _locations.value = _locations.value?.plus(location)
    }


    fun setTrip(trip: TripFirebase) {
        _tripData.value = trip
    }

    fun saveTripToFirebase(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val trip = _tripData.value ?: return
        val locationsList = _locations.value ?: emptyList()

        user?.let { currentUser ->
            val tripWithUser = trip.copy(userId = currentUser.uid)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val tripId = tripRepository.saveTrip(tripWithUser, locationsList)
                    Log.d("TripCreationVM", "Путешествие успешно добавлено: $tripId")

                    withContext(Dispatchers.Main) {
                        onSuccess()
                        clearTripData()
                    }

                } catch (e: Exception) {
                    Log.w("TripCreationVM", "Ошибка при добавлении путешествия", e)

                    withContext(Dispatchers.Main) {
                        onFailure(e)
                    }
                }
            }
        }
    }

    private fun clearTripData() {
        _tripData.value = TripFirebase()
        _locations.value = emptyList()
        _selectedImages.value = emptyList()
    }
}
