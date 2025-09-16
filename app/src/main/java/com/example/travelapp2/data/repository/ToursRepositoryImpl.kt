package com.example.travelapp2.data.repository

import android.util.Log
import com.example.travelapp2.data.models.ChatFirebase
import com.example.travelapp2.data.models.TourFirebase
import com.example.travelapp2.domain.repository.ToursRepository
import com.example.travelapp2.domain.repository.TripRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class ToursRepositoryImpl (
    private val db: FirebaseFirestore
) : ToursRepository {
    private var userToursListener: ListenerRegistration? = null
    private var publicToursListener: ListenerRegistration? = null
    private var tourListener: ListenerRegistration? = null
    private var participantsListener: ListenerRegistration? = null

    override fun listenToUserTours(userId: String, onToursChanged: (List<TourFirebase>) -> Unit) {
        userToursListener?.remove()

        userToursListener = db.collection("tours")
            .whereArrayContains("participants", userId)
            .addSnapshotListener { snapshot, _ ->
                val tours = snapshot?.toObjects(TourFirebase::class.java) ?: emptyList()
                onToursChanged(tours)
            }
    }

    override fun listenToPublicTours(onToursChanged: (List<TourFirebase>) -> Unit) {
        publicToursListener?.remove()

        publicToursListener = db.collection("tours")
            .whereEqualTo("public", true)
            .addSnapshotListener { snapshot, _ ->
                val tours = snapshot?.toObjects(TourFirebase::class.java) ?: emptyList()
                onToursChanged(tours)
            }
    }

    override fun listenToTourChanges(tourId: String, onTourChanged: (TourFirebase?) -> Unit) {
        tourListener?.remove()

        tourListener = db.collection("tours")
            .document(tourId)
            .addSnapshotListener { snapshot, _ ->
                val tour = snapshot?.toObject(TourFirebase::class.java)
                onTourChanged(tour)
            }
    }

    override fun listenToParticipants(tourId: String, onParticipantsChanged: (List<String>) -> Unit) {
        participantsListener?.remove()

        participantsListener = db.collection("tours")
            .document(tourId)
            .addSnapshotListener { snapshot, _ ->
                val participants = snapshot?.get("participants") as? List<String> ?: emptyList()
                onParticipantsChanged(participants)
            }
    }

    override suspend fun updateSavedPlaces(tourId: String, updatedSavedPlaces: Map<String, List<String>>) {
        val tourRef = db.collection("tours").document(tourId)

        try {
            tourRef.update("savedPlacesId", updatedSavedPlaces)
                .await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun fetchParticipantsAvatars(participantIds: List<String>): List<Pair<String, String>> {
        return try {
            val usersCollection = db.collection("users")
            val chunks = participantIds.chunked(10)
            val avatarData = mutableListOf<Pair<String, String>>()

            for (chunk in chunks) {
                val snapshot = usersCollection
                    .whereIn("userId", chunk)
                    .get()
                    .await()

                avatarData += snapshot.documents.mapNotNull { doc ->
                    val id = doc.getString("userId")
                    val photoUrl = doc.getString("photoUrl")
                    if (id != null && photoUrl != null) id to photoUrl else null
                }
            }

            avatarData
        } catch (e: Exception) {
            emptyList()
        }
    }


    override suspend fun getTourById(tourId: String): TourFirebase? {
        val doc = db.collection("tours").document(tourId).get().await()
        return doc.toObject(TourFirebase::class.java)?.copy(id = doc.id)
    }


    override suspend fun updatePlacesByCategory(tourId: String, categoryKey: String, placeIds: List<String>) {
        val tourRef = db.collection("tours").document(tourId)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(tourRef)
            val currentData = snapshot.toObject(TourFirebase::class.java)
            val updatedPlacesByCategory = currentData?.savedPlacesId?.toMutableMap() ?: mutableMapOf()
            updatedPlacesByCategory[categoryKey] = placeIds
            transaction.update(tourRef, "savedPlacesId", updatedPlacesByCategory)
        }.await()
    }


    override suspend fun updateTourRoute(tourId: String, newRoute: List<Map<String, Any>>) {
        val tourRef = FirebaseFirestore.getInstance().collection("tours").document(tourId)
        tourRef.update("route", newRoute)
            .addOnSuccessListener {
                Log.d("ToursRepository", "Маршрут обновлён успешно")
            }
            .addOnFailureListener { e ->
                Log.e("ToursRepository", "Ошибка при обновлении маршрута", e)
            }
    }


    override suspend fun requestToJoinTour(tourId: String, userId: String) {
        val tourRef = FirebaseFirestore.getInstance().collection("tours").document(tourId)
        tourRef.update("requests", FieldValue.arrayUnion(userId)).await()
    }

    private val toursCollection = db.collection("tours")

    override suspend fun approveJoinRequest(tourId: String, userId: String, onComplete: (() -> Unit)?) {
        val tourRef = toursCollection.document(tourId)
        tourRef.update("requests", FieldValue.arrayRemove(userId))
            .addOnSuccessListener {
                tourRef.update("participants", FieldValue.arrayUnion(userId))
                    .addOnSuccessListener { onComplete?.invoke() }
            }
    }

    override suspend fun rejectJoinRequest(tourId: String, userId: String, onComplete: (() -> Unit)?) {
        val tourRef = toursCollection.document(tourId)
        tourRef.update("requests", FieldValue.arrayRemove(userId))
            .addOnSuccessListener { onComplete?.invoke() }
    }

    override suspend fun removeParticipant(tourId: String, userId: String) {
        val tourRef = FirebaseFirestore.getInstance()
            .collection("tours")
            .document(tourId)

        tourRef.update("participants", FieldValue.arrayRemove(userId)).await()
    }

    override suspend fun updateTour(tourId: String, updatedTour: TourFirebase) {
        val tourRef = db.collection("tours").document(tourId)

        tourRef.update(
            "title", updatedTour.title,
            "startDate", updatedTour.startDate,
            "endDate", updatedTour.endDate,
            "city", updatedTour.city,
            "description", updatedTour.description,
            "public", updatedTour.public,
            "maxParticipants", updatedTour.maxParticipants,
            "photoUrl", updatedTour.photoUrl
        ).await()
    }

    override suspend fun deleteTour(tourId: String) {
        try {
            val toursRef = db.collection("tours").document(tourId)

            toursRef.delete().await()

            val chatsQuerySnapshot = db.collection("chats")
                .whereEqualTo("tourId", tourId)
                .get()
                .await()

            for (chatDoc in chatsQuerySnapshot.documents) {
                val chatId = chatDoc.id
                val messagesRef = db.collection("chats").document(chatId).collection("messages")

                val messagesSnapshot = messagesRef.get().await()
                for (messageDoc in messagesSnapshot.documents) {
                    messagesRef.document(messageDoc.id).delete().await()
                }

                db.collection("chats").document(chatId).delete().await()
                Log.d("Firestore", "Чат $chatId удалён вместе с сообщениями")
            }

        } catch (e: Exception) {
            Log.e("Firestore", "Ошибка при удалении тура и связанных чатов", e)
        }
    }

    override suspend fun saveTour(tour: TourFirebase): String = withContext(Dispatchers.IO) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw IllegalStateException("Пользователь не авторизован")


        val isNewTour = tour.id.isEmpty()
        val tourRef = if (isNewTour) db.collection("tours").document() else db.collection("tours").document(tour.id)
        val tourId = tourRef.id
        val chatId = if (isNewTour) UUID.randomUUID().toString() else tour.chatId

        val tourToSave = tour.copy(
            id = tourId,
            chatId = chatId,
            adminId = tour.adminId.ifEmpty { currentUserId },
            participants = tour.participants.ifEmpty { listOf(currentUserId) }
        )

        tourRef.set(tourToSave).await()

        if (isNewTour) {
            val chat = ChatFirebase(
                id = chatId,
                tourId = tourId,
                participants = tourToSave.participants
            )

            db.collection("chats").document(chatId).set(chat).await()
        }

        return@withContext tourId
    }



}
