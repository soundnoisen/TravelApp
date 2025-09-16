package com.example.travelapp2.presentation.routes

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.travelapp2.R
import com.example.travelapp2.main.home.filters.FiltersViewModel
import com.example.travelapp2.presentation.common.LazyColumnRoutes
import com.example.travelapp2.presentation.routes.component.RoutesHeader
import com.example.travelapp2.presentation.viewmodels.RouteFirebaseViewModel
import com.example.travelapp2.presentation.viewmodels.UserPreferencesViewModel

@Composable
fun RoutesScreen(
    navController: NavHostController,
    routeFirebaseViewModel: RouteFirebaseViewModel,
    userPreferencesViewModel: UserPreferencesViewModel,
    filtersViewModel: FiltersViewModel,
) {
    val filter by filtersViewModel.filter.collectAsState()
    val rawRoutes by routeFirebaseViewModel.interestingRoutes.observeAsState(emptyList())
    val filteredRoutes by routeFirebaseViewModel.filteredInteresting.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        routeFirebaseViewModel.loadInterestingRoutes()
    }

    LaunchedEffect(rawRoutes, filter) {
        routeFirebaseViewModel.applyFilterToInteresting(filter)
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))) {
        Spacer(modifier = Modifier.padding(top = 40.dp))
        RoutesHeader(navController) {
            filtersViewModel.clearAll()
            navController.popBackStack()
        }
        LazyColumnRoutes(filteredRoutes, userPreferencesViewModel, navController, routeFirebaseViewModel)
    }
}