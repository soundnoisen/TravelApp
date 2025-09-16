package com.example.travelapp2.di

import com.example.travelapp2.data.firebase.AuthFirebaseDataSource
import com.example.travelapp2.data.firebase.UserProfileFirebaseRepository
import com.example.travelapp2.domain.repository.UserAuthRepository
import com.example.travelapp2.domain.usecase.LoginUseCase
import com.example.travelapp2.domain.usecase.RegisterUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthFirebaseDataSource(auth: FirebaseAuth): AuthFirebaseDataSource {
        return AuthFirebaseDataSource(auth)
    }

    @Provides
    @Singleton
    fun provideUserRepository(authDataSource: AuthFirebaseDataSource): UserAuthRepository {
        return UserProfileFirebaseRepository(authDataSource)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(userAuthRepository: UserAuthRepository): LoginUseCase {
        return LoginUseCase(userAuthRepository)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(userAuthRepository: UserAuthRepository): RegisterUseCase {
        return RegisterUseCase(userAuthRepository)
    }
}
