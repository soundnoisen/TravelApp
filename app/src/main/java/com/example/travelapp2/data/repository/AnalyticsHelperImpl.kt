package com.example.travelapp2.data.repository

import android.os.Bundle
import com.example.travelapp2.data.models.RouteFirebase
import com.example.travelapp2.domain.repository.AnalyticsHelper
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsHelperImpl(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsHelper {
    override fun logRouteView(route: RouteFirebase) {
        val bundle = Bundle().apply {
            putString("route_id", route.id)
            putString("city", route.city)
            putString("difficulty_level", route.difficultyLevel)
            putString("route_time", route.routeTime)
            putString("transport_type", route.typeOfTransport)
        }
        firebaseAnalytics.logEvent("view_route", bundle)
    }
}