package com.example.travelapp2.domain.repository

import com.example.travelapp2.data.models.RouteFirebase

interface AnalyticsHelper {
    fun logRouteView(route: RouteFirebase)
}