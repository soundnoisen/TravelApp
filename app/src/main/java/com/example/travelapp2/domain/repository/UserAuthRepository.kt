package com.example.travelapp2.domain.repository

interface UserAuthRepository {
    suspend fun signIn(email: String, password: String)
    suspend fun register(email: String, password: String): String
}