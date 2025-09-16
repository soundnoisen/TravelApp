package com.example.travelapp2.data.repository

import android.util.Log
import com.example.travelapp2.data.models.LocationPointFirebase
import com.example.travelapp2.data.models.TripFirebase
import com.example.travelapp2.domain.repository.TripRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class TripRepositoryImpl(
    private val db: FirebaseFirestore
) : TripRepository {

    override suspend fun getTripsByUserId(userId: String): List<TripFirebase> {
        return try {
            val snapshot = db.collection("trips")
                .whereEqualTo("userId", userId)
                .get()
                .await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(TripFirebase::class.java)?.copy(id = document.id)
            }
        } catch (e: Exception) {
            Log.e("TripRepository", "Ошибка при получении путешествий: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getTripById(tripId: String): TripFirebase? {
        return try {
            val snapshot = db.collection("trips").document(tripId).get().await()
            if (snapshot.exists()) {
                snapshot.toObject(TripFirebase::class.java)?.copy(id = snapshot.id)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("TripRepository", "Ошибка при получении путешествия по ID: ${e.message}")
            null
        }
    }

    override suspend fun getLocationsByTripId(tripId: String): List<LocationPointFirebase> {
        return try {
            val snapshot = db.collection("trips").document(tripId)
                .collection("locations")
                .get()
                .await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(LocationPointFirebase::class.java)
            }
        } catch (e: Exception) {
            Log.e("TripRepository", "Ошибка при получении локаций: ${e.message}")
            emptyList()
        }
    }

    override suspend fun deleteTrip(tripId: String) {
        val tripRef = db.collection("trips").document(tripId)
        val locationsRef = tripRef.collection("locations")

        try {
            val locationsSnapshot = locationsRef.get().await()
            for (locationDoc in locationsSnapshot.documents) {
                locationsRef.document(locationDoc.id).delete().await()
            }

            tripRef.delete().await()

            Log.d("Firestore", "Путешествие $tripId и все локации успешно удалены")
        } catch (e: Exception) {
            Log.w("Firestore", "Ошибка при удалении путешествия $tripId", e)
        }
    }


    override suspend fun saveTrip(trip: TripFirebase, locations: List<LocationPointFirebase>): String = withContext(Dispatchers.IO) {
        val tripRef = if (trip.id.isNotEmpty()) {
            db.collection("trips").document(trip.id)
        } else {
            db.collection("trips").document()
        }

        val tripWithId = trip.copy(id = tripRef.id)
        tripRef.set(tripWithId).await()

        val locationsCollection = tripRef.collection("locations")
        val existingLocations = locationsCollection.get().await()
        for (doc in existingLocations.documents) {
            doc.reference.delete().await()
        }

        locations.forEach { location ->
            val locationRef = locationsCollection.document()
            val locationWithId = location.copy(id = locationRef.id)
            locationRef.set(locationWithId).await()
        }

        return@withContext tripRef.id
    }




    override suspend fun getPublicTripsByUserId(userId: String): List<TripFirebase> {
        return try {
            val snapshot = db.collection("trips")
                .whereEqualTo("userId", userId)
                .whereEqualTo("public", true)
                .get()
                .await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(TripFirebase::class.java)?.copy(id = document.id)
            }
        } catch (e: Exception) {
            Log.e("TripRepository", "Ошибка при получении путешествий: ${e.message}")
            emptyList()
        }
    }
}
