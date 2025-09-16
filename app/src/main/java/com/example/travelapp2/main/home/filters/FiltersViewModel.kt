package com.example.travelapp2.main.home.filters

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
) : ViewModel() {

    private val _filter = MutableStateFlow(RouteFilter())
    val filter: StateFlow<RouteFilter> = _filter.asStateFlow()

    fun setCity(city: String) =
        update(_filter.value.copy(city = city))

    fun setRouteTime(routeTime: String) =
        update(_filter.value.copy(routeTime = routeTime))

    fun setExactDays(exactDays: String) =
        update(_filter.value.copy(exactDays = exactDays))

    fun setDifficulty(difficulty: String) =
        update(_filter.value.copy(difficulty = difficulty))

    fun setBudget(start: String, end: String) =
        update(_filter.value.copy(budgetStart = start, budgetEnd = end))

    fun setTransportType(transportType: String) =
        update(_filter.value.copy(transportType = transportType))

    fun addRouteType(type: String) =
        update(_filter.value.copy(routeTypes = _filter.value.routeTypes + type))

    fun removeRouteType(type: String) =
        update(_filter.value.copy(routeTypes = _filter.value.routeTypes - type))

    fun clearAll() = update(RouteFilter())

    private fun update(new: RouteFilter) {
        _filter.value = new
    }
}


data class RouteFilter(
    val city: String            = "",
    val routeTime: String       = "",
    val exactDays: String       = "",
    val difficulty: String      = "",
    val budgetStart: String     = "",
    val budgetEnd: String       = "",
    val transportType: String   = "",
    val routeTypes: Set<String> = emptySet()
)
