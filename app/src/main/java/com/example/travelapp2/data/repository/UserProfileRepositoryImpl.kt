package com.example.travelapp2.data.repository

import android.util.Log
import com.example.travelapp2.data.models.GeoApifyPlace
import com.example.travelapp2.data.models.PlaceFirebase
import com.example.travelapp2.data.models.UserProfileFirebase
import com.example.travelapp2.domain.repository.UserProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class UserProfileRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : UserProfileRepository {

    private val collectionName = "users"


    override suspend fun getUserProfile(userId: String): UserProfileFirebase? =
        withContext(Dispatchers.IO) {
            try {
                val snapshot = firestore.collection(collectionName)
                    .document(userId)
                    .get()
                    .await()
                if (snapshot.exists()) {
                    snapshot.toObject(UserProfileFirebase::class.java)
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e("UserProfileRepo", "Ошибка получения профиля: ${e.message}")
                null
            }
        }

    override suspend fun getUsersByIds(
        userIds: List<String>,
        onComplete: (List<UserProfileFirebase>) -> Unit
    ) {
        if (userIds.isEmpty()) {
            onComplete(emptyList())
            return
        }

        FirebaseFirestore.getInstance()
            .collection("users")
            .whereIn("userId", userIds)
            .get()
            .addOnSuccessListener { result ->
                val users = result.documents.mapNotNull { it.toObject(UserProfileFirebase::class.java) }
                onComplete(users)
            }
            .addOnFailureListener {
                onComplete(emptyList())
            }
    }


    override suspend fun saveUserProfile(profile: UserProfileFirebase): Unit =
        withContext(Dispatchers.IO) {
            try {
                firestore.collection(collectionName)
                    .document(profile.userId)
                    .set(profile)
                    .await()
                Log.d("UserProfileRepo", "Профиль успешно сохранен")
            } catch (e: Exception) {
                Log.e("UserProfileRepo", "Ошибка сохранения профиля: ${e.message}")
                throw e
            }
        }

    override suspend fun updateUserProfile(userId: String, updates: Map<String, Any>): Unit =
        withContext(Dispatchers.IO) {
            try {
                firestore.collection(collectionName)
                    .document(userId)
                    .update(updates)
                    .await()
                Log.d("UserProfileRepo", "Профиль успешно обновлен")
            } catch (e: Exception) {
                Log.e("UserProfileRepo", "Ошибка обновления профиля: ${e.message}")
                throw e
            }
        }

    override suspend fun updateSavedRoutes(userId: String, newSavedRoutes: List<String>) {
        withContext(Dispatchers.IO) {
            try {
                firestore.collection(collectionName)
                    .document(userId)
                    .update("savedRoutesId", newSavedRoutes)
                    .await()
                Log.d("UserProfileRepo", "Сохраненные маршруты обновлены")
            } catch (e: Exception) {
                Log.e("UserProfileRepo", "Ошибка обновления сохранённых маршрутов: ${e.message}")
                throw e
            }
        }
    }

    override suspend fun updateSavedPlaces(userId: String, newSavedPlaces: List<String>) {
        withContext(Dispatchers.IO) {
            try {
                firestore.collection(collectionName)
                    .document(userId)
                    .update("savedPlacesId", newSavedPlaces)
                    .await()
                Log.d("UserProfileRepo", "Сохраненные места обновлены")
            } catch (e: Exception) {
                Log.e("UserProfileRepo", "Ошибка обновления сохранённых мест: ${e.message}")
                throw e
            }
        }
    }

    override suspend fun removeSavedRoute(userId: String, routeId: String) {
        withContext(Dispatchers.IO) {
            try {
                val userDocRef = firestore.collection(collectionName).document(userId)

                firestore.runTransaction { transaction ->
                    val snapshot = transaction.get(userDocRef)
                    val currentRoutes = snapshot.get("savedRoutesId") as? List<String> ?: emptyList()
                    val updatedRoutes = currentRoutes.filterNot { it == routeId }
                    transaction.update(userDocRef, "savedRoutesId", updatedRoutes)
                }.await()

                Log.d("UserProfileRepo", "Маршрут удалён из сохранённых")
            } catch (e: Exception) {
                Log.e("UserProfileRepo", "Ошибка удаления маршрута: ${e.message}")
                throw e
            }
        }
    }


    override suspend fun removeSavedPlace(userId: String, placeId: String) {
        withContext(Dispatchers.IO) {
            try {
                val userDocRef = firestore.collection(collectionName).document(userId)

                firestore.runTransaction { transaction ->
                    val snapshot = transaction.get(userDocRef)
                    val currentPlaces = snapshot.get("savedPlacesId") as? List<String> ?: emptyList()
                    val updatedPlaces = currentPlaces.filterNot { it == placeId }
                    transaction.update(userDocRef, "savedPlacesId", updatedPlaces)
                }.await()

                Log.d("UserProfileRepo", "Место удалёно из сохранённых")
            } catch (e: Exception) {
                Log.e("UserProfileRepo", "Ошибка удаления места: ${e.message}")
                throw e
            }
        }
    }


    override suspend fun toggleSavedRoute(userId: String, routeId: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val userDocRef = firestore.collection(collectionName).document(userId)

                val result = firestore.runTransaction { transaction ->
                    val snapshot = transaction.get(userDocRef)
                    val currentRoutes = snapshot.get("savedRoutesId") as? List<String> ?: emptyList()
                    val updatedRoutes: List<String>
                    val isNowSaved: Boolean

                    if (currentRoutes.contains(routeId)) {
                        updatedRoutes = currentRoutes - routeId
                        isNowSaved = false
                    } else {
                        updatedRoutes = currentRoutes + routeId
                        isNowSaved = true
                    }

                    transaction.update(userDocRef, "savedRoutesId", updatedRoutes)
                    isNowSaved
                }.await()

                result
            } catch (e: Exception) {
                Log.e("UserProfileRepo", "Ошибка toggleSavedRoute: ${e.message}")
                throw e
            }
        }
    }

    override suspend fun toggleSavedPlace(userId: String, place: PlaceFirebase): Boolean = withContext(Dispatchers.IO) {
        try {
            val userSnapshot = firestore.collection("users").document(userId).get().await()
            val user = userSnapshot.toObject(UserProfileFirebase::class.java) ?: return@withContext false

            val savedPlaces = user.savedPlacesId.toMutableList()

            Log.i("UserProfileRepo", userId +" "+ savedPlaces+" "+place.name)

            val placesSnapshot = firestore.collection("places")
                .whereEqualTo("name", place.name)
                .whereEqualTo("address", place.address)
                .get()
                .await()

            val existingPlace = placesSnapshot.documents.firstOrNull()
            Log.i("UserProfileRepo", existingPlace.toString())

            val placeId: String

            if (existingPlace != null) {
                placeId = existingPlace.id
            } else {
                val newDoc = firestore.collection("places").document()
                placeId = newDoc.id
                val placeToSave = place.copy(id = placeId)
                newDoc.set(placeToSave).await()
            }

            val isNowSaved: Boolean

            if (savedPlaces.contains(placeId)) {
                savedPlaces.remove(placeId)
                isNowSaved = false
            } else {
                savedPlaces.add(placeId)
                isNowSaved = true
            }

            firestore.collection("users").document(userId).update("savedPlacesId", savedPlaces).await()

            return@withContext isNowSaved
        } catch (e: Exception) {
            Log.e("UserProfileRepo", "Ошибка toggleSavedPlace: ${e.message}")
            return@withContext false
        }
    }


    override suspend fun toggleNowSavedPlace(userId: String, place: PlaceFirebase): Boolean = withContext(Dispatchers.IO) {
        try {
            val userSnapshot = firestore.collection("users").document(userId).get().await()
            val user = userSnapshot.toObject(UserProfileFirebase::class.java) ?: return@withContext false

            val savedPlaces = user.savedPlacesId.toMutableList()

            val placesSnapshot = firestore.collection("places")
                .whereEqualTo("name", place.name)
                .whereEqualTo("address", place.address)
                .get()
                .await()

            val existingPlace = placesSnapshot.documents.firstOrNull()
            Log.i("UserProfileRepo", existingPlace.toString())
            val isNowSaved = savedPlaces.contains(existingPlace?.id ?: "")

            return@withContext isNowSaved
        } catch (e: Exception) {
            Log.e("UserProfileRepo", "Ошибка toggleSavedPlace: ${e.message}")
            return@withContext false
        }
    }

    override suspend fun updateFieldUserProfile(profile: UserProfileFirebase) {
        val userRef = firestore.collection("users").document(profile.userId)

        userRef.update(
            "displayName", profile.displayName,
            "bio", profile.bio,
            "socialLinks", profile.socialLinks,
            "photoUrl", profile.photoUrl
        ).await()
    }

    override suspend fun deleteUserCompletely(userId: String): Boolean {
        return try {
            firestore.collection("users").document(userId).delete().await()
            firestore.collection("usersPreferences").document(userId).delete().await()

            val trips = firestore.collection("trips")
                .whereEqualTo("userId", userId)
                .get()
                .await()
            for (doc in trips.documents) {
                val tripId = doc.id
                val tripsCollection = firestore.collection("trips")
                val tripRef = tripsCollection.document(tripId)

                val locationsSnapshot = tripRef.collection("locations").get().await()
                for (locationDoc in locationsSnapshot.documents) {
                    locationDoc.reference.delete().await()
                }

                doc.reference.delete()
            }

            val routes = firestore.collection("routes")
                .whereEqualTo("userId", userId)
                .get()
                .await()
            for (doc in routes.documents) {
                doc.reference.delete()
            }

            val tours = firestore.collection("tours")
                .whereEqualTo("adminId", userId)
                .get()
                .await()
            for (doc in tours.documents) {
                for (tour in tours.documents) {
                    val chatId = tour.getString("chatId")


                    if (!chatId.isNullOrEmpty()) {
                        val chatsCollection = firestore.collection("chats")
                        val chatRef = chatsCollection.document(chatId)

                        val messagesSnapshot = chatRef.collection("messages").get().await()
                        for (locationDoc in messagesSnapshot.documents) {
                            locationDoc.reference.delete().await()
                        }

                        firestore.collection("chats").document(chatId).delete().await()
                    }

                    tour.reference.delete().await()
                }
            }


            val tours2 = firestore.collection("tours").get().await()
            for (tour in tours2.documents) {
                val participants = tour.get("participants") as? List<String> ?: continue
                if (participants.contains(userId)) {
                    val updatedParticipants = participants.filterNot { it == userId }
                    tour.reference.update("participants", updatedParticipants).await()
                }
                val requests = tour.get("requests") as? List<String> ?: continue
                if (requests.contains(userId)) {
                    val updatedRequests = requests.filterNot { it == userId }
                    tour.reference.update("requests", updatedRequests).await()
                }
            }

            val chats = firestore.collection("chats").get().await()
            for (chat in chats.documents) {
                val participants = chat.get("participants") as? List<String> ?: continue
                if (participants.contains(userId)) {
                    val updatedParticipants = participants.filterNot { it == userId }
                    chat.reference.update("participants", updatedParticipants).await()
                }
            }

            val users = firestore.collection("users").get().await()
            for (user in users.documents) {
                val subscribersId = user.get("subscribersId") as? List<String> ?: continue
                if (subscribersId.contains(userId)) {
                    val updatedSubscribersId = subscribersId.filterNot { it == userId }
                    user.reference.update("subscribersId", updatedSubscribersId).await()
                }

                val subscriptionsId = user.get("subscriptionsId") as? List<String> ?: continue
                if (subscriptionsId.contains(userId)) {
                    val updatedSubscriptionsId = subscriptionsId.filterNot { it == userId }
                    user.reference.update("subscriptionsId", updatedSubscriptionsId).await()
                }
            }

            auth.currentUser?.delete()?.await()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun getUsersByIds(ids: List<String>): List<UserProfileFirebase> {
        return try {
            if (ids.isEmpty()) return emptyList()
            val result = mutableListOf<UserProfileFirebase>()
            ids.chunked(10).forEach { chunk ->
                val snapshot = firestore.collection("users")
                    .whereIn("userId", chunk)
                    .get()
                    .await()

                result += snapshot.documents.mapNotNull { doc ->
                    doc.toObject(UserProfileFirebase::class.java)
                }
            }

            result
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun removeSubscription(currentUserId: String, targetUserId: String) {
        try {
            val currentUserRef = firestore.collection("users").document(currentUserId)
            val targetUserRef = firestore.collection("users").document(targetUserId)

            firestore.runBatch { batch ->
                batch.update(currentUserRef, "subscriptionsId", FieldValue.arrayRemove(targetUserId))
                batch.update(targetUserRef, "subscribersId", FieldValue.arrayRemove(currentUserId))
            }.await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun removeSubscriber(currentUserId: String, subscriberId: String) {
        try {
            val currentUserRef = firestore.collection("users").document(currentUserId)
            val subscriberRef = firestore.collection("users").document(subscriberId)

            firestore.runBatch { batch ->
                batch.update(currentUserRef, "subscribersId", FieldValue.arrayRemove(subscriberId))
                batch.update(subscriberRef, "subscriptionsId", FieldValue.arrayRemove(currentUserId))
            }.await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun addSubscription(currentUserId: String, targetUserId: String) {
        try {
            val currentUserRef = firestore.collection("users").document(currentUserId)
            val targetUserRef = firestore.collection("users").document(targetUserId)

            firestore.runBatch { batch ->
                batch.update(currentUserRef, "subscriptionsId", FieldValue.arrayUnion(targetUserId))
                batch.update(targetUserRef, "subscribersId", FieldValue.arrayUnion(currentUserId))
            }.await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    private var profileListener: ListenerRegistration? = null

    override fun listenToUserProfile(
        userId: String,
        onProfileChanged: (UserProfileFirebase) -> Unit
    ) {
        profileListener?.remove()

        profileListener = firestore.collection("users")
            .document(userId)
            .addSnapshotListener { snapshot, _ ->
                val profile = snapshot?.toObject(UserProfileFirebase::class.java)
                profile?.let { onProfileChanged(it) }
            }
    }


}