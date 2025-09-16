package com.example.travelapp2.data.repository

import android.util.Log
import com.example.travelapp2.data.models.RouteFirebase
import com.example.travelapp2.data.models.UserRoutesPreferences
import com.example.travelapp2.domain.repository.UserPreferencesRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserPreferencesRepositoryImpl(
    private val firestore: FirebaseFirestore
) : UserPreferencesRepository {

    private val collectionName = "usersPreferences"

    override suspend fun getUserPreferences(userId: String): UserRoutesPreferences =
        withContext(Dispatchers.IO) {
            try {
                val snapshot = firestore.collection(collectionName).document(userId).get().await()
                snapshot.toObject(UserRoutesPreferences::class.java) ?: UserRoutesPreferences()
            } catch (e: Exception) {
                Log.e("UserPreferencesRepo", "Ошибка получения предпочтений: ${e.message}")
                UserRoutesPreferences()
            }
        }

    override suspend fun updateUserRoutePreferencesOnSave(userId: String, route: RouteFirebase) {
        updateUserRoutePreferences(userId, route, weight = 1.0)
    }

    override suspend fun updateUserRoutePreferencesOnView(userId: String, route: RouteFirebase) {
        updateUserRoutePreferences(userId, route, weight = 0.2)
    }

    private suspend fun updateUserRoutePreferences(userId: String, route: RouteFirebase, weight: Double) =
        withContext(Dispatchers.IO) {
            val userDocRef = firestore.collection(collectionName).document(userId)
            try {
                val snapshot = userDocRef.get().await()
                val currentPrefs = if (snapshot.exists()) {
                    snapshot.toObject(UserRoutesPreferences::class.java) ?: UserRoutesPreferences()
                } else {
                    UserRoutesPreferences()
                }
                Log.i("UserPreferencesRepo", "Loaded prefs: $currentPrefs")

                route.typesOfRoute.forEach { type ->
                    currentPrefs.preferredTypesOfRoute[type] =
                        (currentPrefs.preferredTypesOfRoute[type] ?: 0.0) + weight
                }
                currentPrefs.preferredRouteTimes[route.routeTime] =
                    (currentPrefs.preferredRouteTimes[route.routeTime] ?: 0.0) + weight
                currentPrefs.preferredDifficultyLevels[route.difficultyLevel] =
                    (currentPrefs.preferredDifficultyLevels[route.difficultyLevel] ?: 0.0) + weight
                currentPrefs.preferredTransportTypes[route.typeOfTransport] =
                    (currentPrefs.preferredTransportTypes[route.typeOfTransport] ?: 0.0) + weight
                currentPrefs.preferredBudgetRanges.add(route.budgetRange)

                Log.i("UserPreferencesRepo", "Saving updated prefs: $currentPrefs")
                userDocRef.set(currentPrefs, SetOptions.merge()).await()
                Log.i("UserPreferencesRepo", "Preferences successfully updated")
            } catch (e: Exception) {
                Log.e("UserPreferencesRepo", "Ошибка при обновлении предпочтений: ${e.message}")
                throw e
            }
        }

    override suspend fun updatePlaceCategoryPreferenceOnSave(userId: String, category: String) {
        updatePlaceCategoryPreference(userId, category, weight = 1.0)
    }

    override suspend fun updatePlaceCategoryPreferenceOnView(userId: String, category: String) {
        updatePlaceCategoryPreference(userId, category, weight = 1.0)
    }

    private suspend fun updatePlaceCategoryPreference(userId: String, category: String, weight: Double) =
        withContext(Dispatchers.IO) {
            val userDocRef = firestore.collection(collectionName).document(userId)
            try {
                val snapshot = userDocRef.get().await()
                val currentPrefs = if (snapshot.exists()) {
                    snapshot.toObject(UserRoutesPreferences::class.java) ?: UserRoutesPreferences()
                } else {
                    UserRoutesPreferences()
                }
                Log.i("UserPreferencesRepo", "Loaded prefs: $currentPrefs")

                currentPrefs.preferredPlaceCategory[category] =
                    (currentPrefs.preferredPlaceCategory[category] ?: 0.0) + weight

                Log.i("UserPreferencesRepo", "Saving updated place category prefs: $currentPrefs")
                userDocRef.set(currentPrefs, SetOptions.merge()).await()
                Log.i("UserPreferencesRepo", "Place category preferences successfully updated")
            } catch (e: Exception) {
                Log.e("UserPreferencesRepo", "Ошибка обновления place category preferences: ${e.message}")
                throw e
            }
        }
}

