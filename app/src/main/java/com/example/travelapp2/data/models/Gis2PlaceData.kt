package com.example.travelapp2.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


data class Place(
    val name: String?,
    val address: String?,
    val addressComment: String?,
    val lat: Double?,
    val lon: Double?,
    val rating: Double?,
    val reviewCount: Int?,
    val mainPhotoUrl: String?,
    val schedule: Map<String, List<WorkingHours>>?,
    val is24x7: Boolean?,
    val comment: String?,
    val fromApi: String?
)

data class ApiResponse(
    val meta: Meta,
    val result: Result
)

data class Meta(
    val api_version: String,
    val code: Int,
    val issue_date: String
)

data class Result(
    val items: List<Item>
)

@JsonClass(generateAdapter = true)
data class Item(
    val id: String?,
    val name: String?,
    @Json(name = "address_name") val addressName: String?,
    @Json(name = "address_comment") val addressComment: String?,
    val point: Point?,
    val reviews: Reviews?,
    @Json(name = "external_content") val externalContent: List<ExternalContent>?,
    val schedule: Schedule?
)

data class Point(
    val lat: Double,
    val lon: Double
)

@JsonClass(generateAdapter = true)
data class Reviews(
    @Json(name = "general_rating") val generalRating: Double?,
    @Json(name = "general_review_count") val generalReviewCount: Int?
)

@JsonClass(generateAdapter = true)
data class ExternalContent(
    @Json(name = "main_photo_url") val mainPhotoUrl: String?
)

@JsonClass(generateAdapter = true)
data class Schedule(
    val is_24x7: Boolean?,
    val Mon: ScheduleDay?,
    val Tue: ScheduleDay?,
    val Wed: ScheduleDay?,
    val Thu: ScheduleDay?,
    val Fri: ScheduleDay?,
    val Sat: ScheduleDay?,
    val Sun: ScheduleDay?,
    @Json(name = "comment") val comment: String?
)

@JsonClass(generateAdapter = true)
data class ScheduleDay(
    @Json(name = "working_hours") val workingHours: List<WorkingHours>?,
    @Json(name = "is_24x7") val is24x7: Boolean?
)

data class WorkingHours(
    val from: String,
    val to: String
)
