package com.example.travelapp2.domain.repository

import com.example.travelapp2.data.models.TourFirebase

interface ToursRepository {
    suspend fun fetchParticipantsAvatars(participantIds: List<String>): List<Pair<String, String>>
    fun listenToUserTours(userId: String, onToursChanged: (List<TourFirebase>) -> Unit)
    fun listenToPublicTours(onToursChanged: (List<TourFirebase>) -> Unit)
    fun listenToTourChanges(tourId: String, onTourChanged: (TourFirebase?) -> Unit)
    fun listenToParticipants(tourId: String, onParticipantsChanged: (List<String>) -> Unit)
    suspend fun updateSavedPlaces(tourId: String, updatedSavedPlaces: Map<String, List<String>>)
    suspend fun getTourById(tourId: String): TourFirebase?
    suspend fun updatePlacesByCategory(tourId: String, categoryKey: String, placeIds: List<String>)
    suspend fun updateTourRoute(tourId: String, newRoute: List<Map<String, Any>>)
    suspend fun requestToJoinTour(tourId: String, userId: String)
    suspend fun approveJoinRequest(tourId: String, userId: String, onComplete: (() -> Unit)? = null)
    suspend fun rejectJoinRequest(tourId: String, userId: String, onComplete: (() -> Unit)? = null)
    suspend fun removeParticipant(tourId: String, userId: String)
    suspend fun updateTour(tourId: String, updatedTour: TourFirebase)
    suspend fun deleteTour(tourId: String)
    suspend fun saveTour(tour: TourFirebase): String
}