package com.example.travelapp2.data.repository

import android.util.Log
import com.example.travelapp2.data.models.RouteFirebase
import com.example.travelapp2.domain.repository.RouteRepository
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RouteRepositoryImpl(
    private val db: FirebaseFirestore
) : RouteRepository {

    override suspend fun getRoutesByUserId(userId: String): List<RouteFirebase> {
        return try {
            val snapshot = db.collection("routes")
                .whereEqualTo("userId", userId)
                .whereEqualTo("generatedByApp", false)
                .get()
                .await()
            snapshot.documents.map { document ->
                document.toObject(RouteFirebase::class.java)?.copy(id = document.id)
            }.filterNotNull()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }


    override suspend fun getInterestingRoutes(): List<RouteFirebase> {
        return try {
            val snapshot = db.collection("routes")
                .whereEqualTo("public", true)
                .get()
                .await()

            val allRoutes = snapshot.documents.mapNotNull { doc ->
                doc.toObject(RouteFirebase::class.java)
                    ?.copy(id = doc.id)
            }

            allRoutes
                .sortedByDescending { it.viewCount + it.saveCount }
                .take(50)
        } catch (e: Exception) {
            Log.e("RouteRepository", "Ошибка извлечения интересных маршрутов", e)
            emptyList()
        }
    }

    override suspend fun getSavedRoutes(userId: String): List<RouteFirebase> {
        return try {
            val userDoc = db.collection("users")
                .document(userId)
                .get()
                .await()

            val savedIds = userDoc.get("savedRoutesId") as? List<*>
                ?: emptyList<Any>()

            if (savedIds.isEmpty()) return emptyList()

            val routeIds = savedIds.filterIsInstance<String>()

            val chunks = routeIds.chunked(10)

            val result = mutableListOf<RouteFirebase>()
            for (chunk in chunks) {
                val snapshot = db.collection("routes")
                    .whereIn(FieldPath.documentId(), chunk)
                    .get()
                    .await()

                val routes = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(RouteFirebase::class.java)
                        ?.copy(id = doc.id)
                }
                result += routes
            }
            result
        } catch (e: Exception) {
            Log.e("RouteRepository", "Ошибка извлечения сохраненных маршрутов", e)
            emptyList()
        }
    }

    override suspend fun getRouteById(routeId: String): RouteFirebase? {
        return try {
            val snapshot = db.collection("routes").document(routeId).get().await()
            if (snapshot.exists()) {
                snapshot.toObject(RouteFirebase::class.java)?.copy(id = snapshot.id)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun deleteRoute(routeId: String) {
        val routeDoc = db.collection("routes").document(routeId)

        routeDoc.delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Маршрут $routeId успешно удален")

                db.collection("users")
                    .whereArrayContains("savedRoutesId", routeId)
                    .get()
                    .addOnSuccessListener { result ->
                        for (userDoc in result) {
                            db.collection("users").document(userDoc.id)
                                .update("savedRoutesId", FieldValue.arrayRemove(routeId))
                                .addOnSuccessListener {
                                    Log.d("Firestore", "Удалён маршрут $routeId из профиля пользователя ${userDoc.id}")
                                }
                                .addOnFailureListener {
                                    Log.w("Firestore", "Не удалось удалить $routeId из пользователя ${userDoc.id}", it)
                                }
                        }
                    }
                    .addOnFailureListener {
                        Log.w("Firestore", "Ошибка при получении пользователей с сохранённым маршрутом $routeId", it)
                    }
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Ошибка при удалении маршрута $routeId", exception)
            }
    }

    override suspend fun saveRoute(route: RouteFirebase): String = withContext(Dispatchers.IO) {
        try {
            val routeRef = if (route.id.isNotEmpty()) {
                db.collection("routes").document(route.id)
            } else {
                db.collection("routes").document()
            }

            val routeWithId = route.copy(id = routeRef.id)

            routeRef.set(routeWithId).await()
            Log.d("RouteCreationRepo", "Маршрут сохранён (id: ${routeRef.id})")
            return@withContext routeRef.id
        } catch (e: Exception) {
            Log.w("RouteCreationRepo", "Ошибка при сохранении маршрута", e)
            throw e
        }
    }


    override suspend fun getPublicRoutesUser(userId: String): List<RouteFirebase> {
        return try {
            val snapshot = db.collection("routes")
                .whereEqualTo("public", true)
                .whereEqualTo("userId", userId)
                .whereEqualTo("generatedByApp", false)
                .get()
                .await()
            snapshot.documents.map { document ->
                document.toObject(RouteFirebase::class.java)?.copy(id = document.id)
            }.filterNotNull()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }


    override suspend fun incrementViewCount(routeId: String) {
        db.collection("routes").document(routeId)
            .update("viewCount", FieldValue.increment(1))
            .await()
    }

    override suspend fun incrementSaveCount(routeId: String) {
        db.collection("routes").document(routeId)
            .update("saveCount", FieldValue.increment(1))
            .await()
    }
}