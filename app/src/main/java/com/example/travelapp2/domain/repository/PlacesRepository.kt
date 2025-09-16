package com.example.travelapp2.domain.repository

import com.example.travelapp2.data.models.PlaceFirebase

interface PlacesRepository {
    suspend fun fetchPlacesByIds(ids: List<String>): List<PlaceFirebase>
}