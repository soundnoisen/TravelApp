package com.example.travelapp2.domain.repository

import com.example.travelapp2.data.models.LocationPointFirebase
import com.example.travelapp2.data.models.TripFirebase

interface TripRepository {
    suspend fun getTripsByUserId(userId: String): List<TripFirebase>
    suspend fun getTripById(tripId: String): TripFirebase?
    suspend fun getLocationsByTripId(tripId: String): List<LocationPointFirebase>
    suspend fun saveTrip(trip: TripFirebase, locations: List<LocationPointFirebase>): String
    suspend fun getPublicTripsByUserId(userId: String): List<TripFirebase>
    suspend fun deleteTrip(tripId: String)
}
