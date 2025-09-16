package com.example.travelapp2.data.network

import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface ApiService {
    @POST("recommend")
    suspend fun getRecommendations(
        @Body userProfile: UserProfileRequest
    ): List<Place>

    @POST("generate")
    suspend fun getGeneratedRoutes(@Body request: UserProfileRequest): Response<List<Route>>

    @POST("recommend_routes_by_preferences")
    suspend fun getRouteRecommendations(
        @Body request: RouteRecommendationRequest
    ): Response<RecommendationResponse>
}

object RetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:5000/"

    val apiService: ApiService by lazy {
        val client = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
