package com.example.travelapp2.presentation.viewmodels

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import android.location.Geocoder
import androidx.core.content.ContextCompat

import com.google.android.gms.location.FusedLocationProviderClient
import java.util.*
import android.Manifest
import android.util.Log
import com.example.travelapp2.presentation.util.locations
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    application: Application,
    private val fusedLocationClient: FusedLocationProviderClient
) : AndroidViewModel(application) {

    private val _city = MutableStateFlow("Местоположение не определено")
    val city: StateFlow<String> get() = _city

    private val _cityCoordinates = MutableStateFlow<Pair<Double, Double>?>(null)
    val cityCoordinates: StateFlow<Pair<Double, Double>?> get() = _cityCoordinates

    private val _coordinates = MutableStateFlow<Pair<Double, Double>?>(null)
    val coordinates: StateFlow<Pair<Double, Double>?> get() = _coordinates


    private val _cityId2Gis = MutableStateFlow<String?>(null)
    val cityId2Gis: StateFlow<String?> get() = _cityId2Gis


    fun getLocation(context: Context) {
        when {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                fetchLocation()
            }
            else -> {
                _city.value = "Необходимо разрешение на доступ к местоположению"
            }
        }
    }

    private fun fetchLocation() {
        viewModelScope.launch {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        val cityName = getCityNameFromCoordinates(55.746177, 49.210592) // val cityName = getCityNameFromCoordinates(location.latitude, location.longitude)
                        _city.value = cityName
                        _cityCoordinates.value = getCoordinatesByCityName(_city.value)
                        _cityId2Gis.value = getId2Gis(_city.value)
                        _coordinates.value = Pair(55.746177, 49.210592) // _coordinates.value = Pair(location.latitude, location.longitude)

                        Log.i("LocationUSer", _cityId2Gis.value.toString() + " " + _coordinates.value )
                    } else {
                        _city.value = "Не удалось получить местоположение"
                    }
                }
            } catch (e: SecurityException) {
                _city.value = "Ошибка безопасности: разрешение не предоставлено"
            }
        }
    }

    fun getCityNameFromCoordinates(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(getApplication(), Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            addresses?.firstOrNull()?.locality ?: "Неизвестно"
        } catch (e: Exception) {
            "Неизвестно"
        }
    }

    private fun getCoordinatesByCityName(cityName: String): Pair<Double, Double>? {
        val geocoder = Geocoder(getApplication(), Locale.getDefault())
        val addresses = geocoder.getFromLocationName(cityName, 1)

        return if (!addresses.isNullOrEmpty()) {
            val location = addresses[0]
            Pair(location.latitude, location.longitude)
        } else {
            null
        }
    }


    private fun getId2Gis(cityName: String): String? {
        return locations[cityName]
    }
}




