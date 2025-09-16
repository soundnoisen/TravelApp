package com.example.travelapp2.data.models

data class UserRoutesPreferences(
    var preferredTypesOfRoute: MutableMap<String, Double> = mutableMapOf(),
    var preferredRouteTimes: MutableMap<String, Double> = mutableMapOf(),
    var preferredDifficultyLevels: MutableMap<String, Double> = mutableMapOf(),
    var preferredTransportTypes: MutableMap<String, Double> = mutableMapOf(),
    var preferredBudgetRanges: MutableList<Map<String, Int>> = mutableListOf(),
    var preferredPlaceCategory: MutableMap<String, Double> = mutableMapOf()
)
