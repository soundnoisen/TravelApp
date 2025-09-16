package com.example.travelapp2.domain.repository

import com.example.travelapp2.data.models.LocationPointFirebase

interface LocationPointRepository {
    suspend fun getLocationsByTripId(tripId: String): List<LocationPointFirebase>
    suspend fun addLocationToTrip(tripId: String, location: LocationPointFirebase): Boolean
}
