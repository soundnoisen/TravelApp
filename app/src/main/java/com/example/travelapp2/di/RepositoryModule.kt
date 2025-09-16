package com.example.travelapp2.di

import com.example.travelapp2.data.repository.ChatRepositoryImpl
import com.example.travelapp2.data.repository.LocationPointRepositoryImpl
import com.example.travelapp2.data.repository.PlacesRepositoryImpl
import com.example.travelapp2.data.repository.RecommendationRepositoryImpl
import com.example.travelapp2.data.repository.RouteRepositoryImpl
import com.example.travelapp2.data.repository.ToursRepositoryImpl
import com.example.travelapp2.data.repository.TripRepositoryImpl
import com.example.travelapp2.data.repository.UserPreferencesRepositoryImpl
import com.example.travelapp2.data.repository.UserProfileRepositoryImpl
import com.example.travelapp2.domain.repository.ChatRepository
import com.example.travelapp2.domain.repository.LocationPointRepository
import com.example.travelapp2.domain.repository.PlacesRepository
import com.example.travelapp2.domain.repository.RecommendationRepository
import com.example.travelapp2.domain.repository.RouteRepository
import com.example.travelapp2.domain.repository.ToursRepository
import com.example.travelapp2.domain.repository.TripRepository
import com.example.travelapp2.domain.repository.UserPreferencesRepository
import com.example.travelapp2.domain.repository.UserProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRouteRepository(db: FirebaseFirestore): RouteRepository {
        return RouteRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideTripRepository(db: FirebaseFirestore): TripRepository {
        return TripRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(firestore: FirebaseFirestore): UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideLocationPointRepository(db: FirebaseFirestore): LocationPointRepository {
        return LocationPointRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideUserProfileRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): UserProfileRepository {
        return UserProfileRepositoryImpl(firestore, auth)
    }

    @Provides
    @Singleton
    fun provideRecommendationRepository(db: FirebaseFirestore): RecommendationRepository {
        return RecommendationRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideToursRepository(db: FirebaseFirestore): ToursRepository {
        return ToursRepositoryImpl(db)
    }


    @Provides
    @Singleton
    fun provideChatRepository(
        firestore: FirebaseFirestore
    ): ChatRepository = ChatRepositoryImpl(firestore)

    @Provides
    fun providePlacesRepository(
        db: FirebaseFirestore
    ): PlacesRepository = PlacesRepositoryImpl(db)
}
