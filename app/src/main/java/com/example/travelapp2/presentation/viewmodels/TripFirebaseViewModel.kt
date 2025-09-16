package com.example.travelapp2.presentation.viewmodels

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
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TripFirebaseViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _userTrips = MutableLiveData<List<TripFirebase>>()
    val userTrips: LiveData<List<TripFirebase>> get() = _userTrips

    private val _userPublicTrips = MutableLiveData<List<TripFirebase>>()
    val userPublicTrips: LiveData<List<TripFirebase>> get() = _userPublicTrips


    fun getTripsByUserId(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val trips = tripRepository.getTripsByUserId(userId)
                _userTrips.postValue(trips)
                Log.d("TripFirebaseVM", "Загружено путешествий: ${trips.size}")
            } catch (e: Exception) {
                Log.w("TripFirebaseVM", "Ошибка при загрузке путешествий", e)
            }
        }
    }

    fun getPublicTripsByUserId(userId: String) {
        //addTestTripToFirebase()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val trips = tripRepository.getPublicTripsByUserId(userId)
                _userPublicTrips.postValue(trips)
                Log.d("TripFirebaseVM", "Загружено путешествий: ${trips.size}")
            } catch (e: Exception) {
                Log.w("TripFirebaseVM", "Ошибка при загрузке путешествий", e)
            }
        }
    }



    fun getTripById(tripId: String): LiveData<TripFirebase?> {
        val liveData = MutableLiveData<TripFirebase?>()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val trip = tripRepository.getTripById(tripId)
                liveData.postValue(trip)
            } catch (e: Exception) {
                Log.w("TripFirebaseVM", "Ошибка при загрузке путешествия по ID", e)
            }
        }
        return liveData
    }

    fun getLocationsByTripId(tripId: String): LiveData<List<LocationPointFirebase>> {
        val liveData = MutableLiveData<List<LocationPointFirebase>>()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val locations = tripRepository.getLocationsByTripId(tripId)
                liveData.postValue(locations)
            } catch (e: Exception) {
                Log.w("TripFirebaseVM", "Ошибка при получении локаций по ID", e)
            }
        }
        return liveData
    }


    fun deleteTrip(tripId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                tripRepository.deleteTrip(tripId)
            } catch (e: Exception) {
                Log.w("TripFirebaseVM", "Ошибка при удалении путешествия: ${e.message}")
            }
        }
    }

}
