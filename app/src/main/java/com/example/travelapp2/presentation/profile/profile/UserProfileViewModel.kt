package com.example.travelapp2.presentation.profile.profile

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp2.data.models.PlaceFirebase
import com.example.travelapp2.data.models.UserProfileFirebase
import com.example.travelapp2.domain.repository.UserProfileRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: UserProfileRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    val currentUser: String = auth.currentUser?.uid ?: throw IllegalStateException("Пользователь не авторизован")

    private val _profile = MutableLiveData<UserProfileFirebase?>()
    val profile: LiveData<UserProfileFirebase?> get() = _profile

    val deletionResult = MutableLiveData<Boolean>()

    private val _subscriptions = MutableLiveData<List<UserProfileFirebase>>()
    val subscriptions: LiveData<List<UserProfileFirebase>> = _subscriptions

    private val _subscribers = MutableLiveData<List<UserProfileFirebase>>()
    val subscribers: LiveData<List<UserProfileFirebase>> = _subscribers

    private var userId: String = ""

    fun setUserId(userId: String) {
        this.userId = userId
        listenToProfile()
    }

    private fun listenToProfile() {
        repository.listenToUserProfile(userId) { updatedProfile ->
            _profile.value = updatedProfile

            viewModelScope.launch {
                val subs = repository.getUsersByIds(updatedProfile.subscriptionsId)
                _subscriptions.value = subs

                val subscr = repository.getUsersByIds(updatedProfile.subscribersId)
                _subscribers.value = subscr
            }
        }
    }

    fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            try {
                val userProfile = repository.getUserProfile(userId)
                _profile.postValue(userProfile)
                Log.d("UserProfileVM", "Профиль загружен: $userId")
            } catch (e: Exception) {
                Log.e("UserProfileVM", "Ошибка при загрузке профиля", e)
            }
        }
    }


    fun deleteUserAccount(userId: String) {
        viewModelScope.launch {
            val result = repository.deleteUserCompletely(userId)
            deletionResult.postValue(result)
        }
    }


    @SuppressLint("NullSafeMutableLiveData")
    fun getUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            val loadedProfile = repository.getUserProfile(currentUser)
            if (loadedProfile != null) {
                withContext(Dispatchers.Main) {
                    _profile.value = loadedProfile
                }
            }
        }
    }


    fun updateUserProfile(profile: UserProfileFirebase) {
        viewModelScope.launch {
            try {
                repository.updateFieldUserProfile(profile)
                Log.d("ProfileUpdate", "Профиль обновлён")
            } catch (e: Exception) {
                Log.e("ProfileUpdate", "Ошибка обновления профиля", e)
            }
        }
    }

    fun removeSubscription(targetUserId: String) {
        viewModelScope.launch {
            repository.removeSubscription(currentUser, targetUserId)
        }
    }

    fun removeSubscriber(targetUserId: String) {
        viewModelScope.launch {
            repository.removeSubscriber(currentUser, targetUserId)
        }
    }

    fun addSubscription(targetUserId: String) {
        viewModelScope.launch {
            repository.addSubscription(currentUser, targetUserId)
        }
    }

    fun clearProfile() {
        _profile.value = null
        _subscribers.value = emptyList()
        _subscriptions.value = emptyList()
    }


    fun toggleSavedRoute(routeId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val isNowSaved = repository.toggleSavedRoute(currentUser, routeId)
                getUserProfile()
                withContext(Dispatchers.Main) {
                    onResult(isNowSaved)
                }
            } catch (e: Exception) {
                Log.e("UserProfileVM", "Ошибка toggleSavedRoute: ${e.message}")
                withContext(Dispatchers.Main) {
                    onResult(false)
                }
            }
        }
    }


    fun toggleSavedPlace(place: PlaceFirebase, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val isNowSaved = repository.toggleSavedPlace(currentUser, place)
                getUserProfile()
                withContext(Dispatchers.Main) {
                    onResult(isNowSaved)
                }
            } catch (e: Exception) {
                Log.e("UserProfileVM", "Ошибка toggleSavedPlace: ${e.message}")
                withContext(Dispatchers.Main) {
                    onResult(false)
                }
            }
        }
    }

    fun toggleNowSavedPlace(place: PlaceFirebase, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val isNowSaved = repository.toggleNowSavedPlace(currentUser, place)
                withContext(Dispatchers.Main) {
                    onResult(isNowSaved)
                }
            } catch (e: Exception) {
                Log.e("UserProfileVM", "Ошибка toggleSavedPlace: ${e.message}")
                withContext(Dispatchers.Main) {
                    onResult(false)
                }
            }
        }
    }



}