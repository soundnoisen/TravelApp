package com.example.travelapp2.presentation.tourCreate

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp2.data.models.TourFirebase
import com.example.travelapp2.data.models.TripFirebase
import com.example.travelapp2.domain.repository.RouteRepository
import com.example.travelapp2.domain.repository.ToursRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject



@HiltViewModel
class TourCreationViewModel @Inject constructor(
    private val tourRepository: ToursRepository,
    private val auth: FirebaseAuth
) : ViewModel() {
    private val user = auth.currentUser

    private val _tourData = MutableLiveData(TourFirebase())
    val tourData: LiveData<TourFirebase> get() = _tourData

    fun setTour(tour: TourFirebase) {
        _tourData.value = tour
    }

    fun saveTourToFirebase(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val tour = _tourData.value ?: return

        user?.let { currentUser ->
            val tourWithUser = tour.copy(adminId = currentUser.uid)
            viewModelScope.launch {
                try {
                    val tripId = tourRepository.saveTour(tourWithUser)
                    Log.d("RouteCreationVM", "Маршрут успешно добавлен: $tripId")
                    onSuccess()
                    clearTourData()
                } catch (e: Exception) {
                    Log.w("RouteCreationVM", "Ошибка при добавлении маршрута", e)
                    onFailure(e)
                }
            }
        }
    }

    private fun clearTourData() {
        _tourData.value = TourFirebase()
    }

    fun updateTitle(title: String) {
        _tourData.value = _tourData.value?.copy(title = title)
    }

    fun updateCity(city: String) {
        _tourData.value = _tourData.value?.copy(city = city)
    }

    fun updateDates(startDate: String, endDate: String) {
        _tourData.value = _tourData.value?.copy(startDate = startDate, endDate = endDate)
    }

    fun updateIsPublic(isPublic: Boolean) {
        _tourData.value = _tourData.value?.copy(public = isPublic)
    }

    fun updateMaxParticipants(maxParticipants: Int) {
        _tourData.value = _tourData.value?.copy(maxParticipants = maxParticipants)
    }

}