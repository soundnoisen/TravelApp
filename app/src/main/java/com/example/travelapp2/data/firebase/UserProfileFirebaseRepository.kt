package com.example.travelapp2.data.firebase

import com.example.travelapp2.domain.repository.UserAuthRepository


class UserProfileFirebaseRepository(private val authDataSource: AuthFirebaseDataSource) : UserAuthRepository {
    override suspend fun signIn(email: String, password: String) {
        authDataSource.signIn(email, password)
    }

    override suspend fun register(email: String, password: String): String {
        val authResult = authDataSource.register(email, password)
        val userId = authResult.user?.uid ?: throw Exception("Ошибка регистрации: UID отсутствует.")
        return userId
    }
}

