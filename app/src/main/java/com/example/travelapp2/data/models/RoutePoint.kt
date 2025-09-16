package com.example.travelapp2.data.models

import org.osmdroid.util.GeoPoint

data class RoutePoint(
    val geoPoint: GeoPoint,
    val name: String = "",
    val address: String = "",
    val city: String = ""
)
