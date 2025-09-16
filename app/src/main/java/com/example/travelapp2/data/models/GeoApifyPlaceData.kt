package com.example.travelapp2.data.models



data class GeoApifyPlace(
    val name: String?,
    val address: String?,
    val lat: Double?,
    val lon: Double?,
    val phone: String?,
    val website: String?,
    val description: String?,
    val categories: List<String>?,
    val openingHours: String?,
    val fromApi: String?,
)

data class GeoApifyApiResponse(
    val type: String,
    val features: List<Feature>?
)

data class Feature(
    val type: String,
    val properties: Properties?,
    val geometry: Geometry?
)

data class Properties(
    val name: String?,
    val address_line2: String?,
    val contact: Contact?,
    val categories: List<String>?,
    val description: String?,
    val opening_hours: String?
)

data class Contact(
    val phone: String?,
    val website: String?
)

data class Geometry(
    val type: String,
    val coordinates: List<Double>?
)

data class Coordinates(
    val lat: Double,
    val lon: Double
)