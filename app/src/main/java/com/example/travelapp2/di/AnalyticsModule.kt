package com.example.travelapp2.di

import android.content.Context
import com.example.travelapp2.data.repository.AnalyticsHelperImpl
import com.example.travelapp2.domain.repository.AnalyticsHelper
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {
    @Provides
    @Singleton
    fun provideAnalyticsHelper(
        @ApplicationContext context: Context
    ): AnalyticsHelper {
        return AnalyticsHelperImpl(FirebaseAnalytics.getInstance(context))
    }
}
