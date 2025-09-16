package com.example.travelapp2.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp2.data.models.RouteFirebase
import com.example.travelapp2.data.models.UserRoutesPreferences
import com.example.travelapp2.domain.repository.AnalyticsHelper
import com.example.travelapp2.domain.repository.UserPreferencesRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

import kotlinx.coroutines.withContext


@HiltViewModel
class UserPreferencesViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val analyticsHelper: AnalyticsHelper,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val userId: String = auth.currentUser?.uid ?: throw IllegalStateException("Пользователь не авторизован")

    fun trackAndSaveRouteView(route: RouteFirebase) {
        analyticsHelper.logRouteView(route)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userPreferencesRepository.updateUserRoutePreferencesOnSave(userId, route)
            } catch (e: Exception) {
                Log.e("UserPreferencesVM", "Ошибка обновления предпочтений: ${e.message}")
            }
        }
    }

    fun updatePlaceCategoryPreferenceOnView(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userPreferencesRepository.updatePlaceCategoryPreferenceOnView(userId, category)
            } catch (e: Exception) {
                Log.e("UserPreferencesVM", "Ошибка при обновлении предпочтения категории (просмотр): ${e.message}")
            }
        }
    }
}


