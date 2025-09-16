package com.example.travelapp2.presentation.registration


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp2.data.models.UserProfileFirebase
import com.example.travelapp2.domain.repository.UserProfileRepository
import com.example.travelapp2.domain.usecase.RegisterUseCase
import com.example.travelapp2.domain.validation.ErrorMessageProvider
import com.example.travelapp2.domain.validation.InputValidator
import com.example.travelapp2.presentation.profile.profile.UserProfileViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val registerUseCase: RegisterUseCase, private val userProfileRepository: UserProfileRepository) : ViewModel() {

    private val _registerSuccess = MutableStateFlow(false)
    val registerSuccess: StateFlow<Boolean> = _registerSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun register(email: String, password: String, passwordRepeat: String) {
        val validation = InputValidator.validateRegistration(email, password, passwordRepeat)
        if (validation != null) {
            _errorMessage.value = validation
            return
        }

        viewModelScope.launch {
            try {
                val userId = registerUseCase.execute(email, password)
                val userProfile = UserProfileFirebase(userId = userId, email = email)
                userProfileRepository.saveUserProfile(userProfile)
                _registerSuccess.value = true
            } catch (ex: Exception) {
                _errorMessage.value = ErrorMessageProvider.getFirebaseErrorMessage(ex)
            }
        }
    }

    fun resetError() {
        _errorMessage.value = null
    }
}
