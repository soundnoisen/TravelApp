package com.example.travelapp2.presentation.login


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp2.domain.usecase.LoginUseCase
import com.example.travelapp2.domain.validation.ErrorMessageProvider
import com.example.travelapp2.domain.validation.InputValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun login(email: String, password: String, context: Context) {
        val validation = InputValidator.validateLogin(email, password)
        if (validation != null) {
            _errorMessage.value = validation
            return
        }

        viewModelScope.launch {
            try {
                loginUseCase.execute(email, password)
                val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
                prefs.edit().putBoolean("is_logged_in", true).apply()

                _loginSuccess.value = true
            } catch (ex: Exception) {
                _errorMessage.value = ErrorMessageProvider.getFirebaseErrorMessage(ex)
            }
        }
    }


    fun resetError() {
        _errorMessage.value = null
    }

}
