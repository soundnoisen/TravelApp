package com.example.travelapp2.presentation.routes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.travelapp2.R
import com.example.travelapp2.presentation.common.LazyColumnRoutes
import com.example.travelapp2.presentation.routes.component.RoutesHeader
import com.example.travelapp2.presentation.viewmodels.RouteFirebaseViewModel
import com.example.travelapp2.presentation.viewmodels.UserPreferencesViewModel
import com.example.travelapp2.presentation.viewmodels.UserRecommendationViewModel


@Composable
fun RoutesRecommendationScreen(
    navController: NavHostController,
    userRecommendationViewModel: UserRecommendationViewModel,
    userPreferencesViewModel: UserPreferencesViewModel,
    routeFirebaseViewModel: RouteFirebaseViewModel
) {
    val routes by userRecommendationViewModel.routesRecommendation.observeAsState(emptyList())

    Column(
        Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))) {
        Spacer(modifier = Modifier.padding(top = 30.dp))
        RoutesHeader(navController) {
            navController.popBackStack()
        }
        LazyColumnRoutes(routes, userPreferencesViewModel, navController, routeFirebaseViewModel)
    }
}