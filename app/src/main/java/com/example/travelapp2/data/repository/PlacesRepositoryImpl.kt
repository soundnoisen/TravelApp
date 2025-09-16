package com.example.travelapp2.data.repository

import com.example.travelapp2.data.models.PlaceFirebase
import com.example.travelapp2.domain.repository.PlacesRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : PlacesRepository {

    override suspend fun fetchPlacesByIds(ids: List<String>): List<PlaceFirebase> {
        if (ids.isEmpty()) return emptyList()

        return try {
            val chunks = ids.chunked(10)
            val result = mutableListOf<PlaceFirebase>()

            for (chunk in chunks) {
                val snapshot = db.collection("places")
                    .whereIn("id", chunk)
                    .get()
                    .await()

                result += snapshot.documents.mapNotNull { it.toObject(PlaceFirebase::class.java) }
            }

            result
        } catch (e: Exception) {
            emptyList()
        }
    }
}
