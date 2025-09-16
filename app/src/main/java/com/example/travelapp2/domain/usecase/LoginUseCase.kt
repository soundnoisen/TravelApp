package com.example.travelapp2.domain.usecase

import com.example.travelapp2.domain.repository.UserAuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val userAuthRepository: UserAuthRepository) {
    suspend fun execute(email: String, password: String) {
        userAuthRepository.signIn(email, password)
    }
}