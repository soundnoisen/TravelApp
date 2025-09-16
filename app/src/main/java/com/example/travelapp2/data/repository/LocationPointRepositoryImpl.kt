package com.example.travelapp2.data.repository

import android.util.Log
import com.example.travelapp2.data.models.LocationPointFirebase
import com.example.travelapp2.domain.repository.LocationPointRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class LocationPointRepositoryImpl(
    private val db: FirebaseFirestore
) : LocationPointRepository {

    override suspend fun getLocationsByTripId(tripId: String): List<LocationPointFirebase> {
        return try {
            val snapshot = db.collection("trips")
                .document(tripId)
                .collection("locations")
                .get()
                .await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(LocationPointFirebase::class.java)?.copy(id = document.id)
            }
        } catch (e: Exception) {
            Log.e("LocationPointRepo", "Ошибка загрузки локаций: ${e.message}")
            emptyList()
        }
    }

    override suspend fun addLocationToTrip(tripId: String, location: LocationPointFirebase): Boolean {
        return try {
            val newLocationRef = db.collection("trips")
                .document(tripId)
                .collection("locations")
                .document()
            val locationWithId = location.copy(id = newLocationRef.id)
            newLocationRef.set(locationWithId).await()
            Log.d("LocationPointRepo", "Точка успешно добавлена: ${newLocationRef.id}")
            true
        } catch (e: Exception) {
            Log.e("LocationPointRepo", "Ошибка добавления точки: ${e.message}")
            false
        }
    }
}
