package com.example.travelapp2.domain.usecase

import com.example.travelapp2.domain.repository.UserAuthRepository

class RegisterUseCase(private val userAuthRepository: UserAuthRepository) {
    suspend fun execute(email: String, password: String): String {
        return userAuthRepository.register(email, password)
    }
}