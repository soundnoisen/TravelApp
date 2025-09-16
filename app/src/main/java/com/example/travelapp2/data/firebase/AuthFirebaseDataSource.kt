package com.example.travelapp2.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.tasks.await

class AuthFirebaseDataSource(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    suspend fun signIn(email: String, password: String): AuthResult {
        return auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun register(email: String, password: String): AuthResult {
        return auth.createUserWithEmailAndPassword(email, password).await()
    }
}


