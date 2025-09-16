package com.example.travelapp2.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.travelapp2.presentation.viewmodels.UserRecommendationViewModel
import com.example.travelapp2.main.home.filters.FiltersScreen
import com.example.travelapp2.main.home.filters.FiltersViewModel
import com.example.travelapp2.presentation.home.HomeScreen
import com.example.travelapp2.presentation.profile.createRoute.RouteCreationViewModel
import com.example.travelapp2.presentation.profile.profile.UserProfileViewModel
import com.example.travelapp2.presentation.route.RouteScreen
import com.example.travelapp2.presentation.routes.RoutesRecommendationScreen
import com.example.travelapp2.presentation.routes.RoutesScreen
import com.example.travelapp2.presentation.viewmodels.CategorySearchViewModel
import com.example.travelapp2.presentation.viewmodels.GIS2ViewModel
import com.example.travelapp2.presentation.viewmodels.LocationViewModel
import com.example.travelapp2.presentation.viewmodels.RouteFirebaseViewModel
import com.example.travelapp2.presentation.viewmodels.UserPreferencesViewModel

@Composable
fun Home(
    locationViewModel: LocationViewModel,
    mapViewModel: GIS2ViewModel,
    mainNavController: NavHostController,
    categorySearchViewModel: CategorySearchViewModel,
    routeFirebaseViewModel: RouteFirebaseViewModel,
    userRecommendationViewModel: UserRecommendationViewModel,
    userPreferencesViewModel: UserPreferencesViewModel,
    userProfileViewModel: UserProfileViewModel,
    routeCreationViewModel: RouteCreationViewModel,
    filtersViewModel: FiltersViewModel
) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "HomeScreen") {
        composable("HomeScreen") {
            HomeScreen(locationViewModel, mapViewModel, navController, mainNavController, categorySearchViewModel, routeFirebaseViewModel, userRecommendationViewModel, userPreferencesViewModel)
        }
        composable("RoutesScreen") {
            RoutesScreen(navController, routeFirebaseViewModel, userPreferencesViewModel, filtersViewModel)
        }
        composable("RoutesRecommendation") {
            RoutesRecommendationScreen(navController, userRecommendationViewModel, userPreferencesViewModel, routeFirebaseViewModel)
        }
        composable("RouteScreen/{routeId}") { backStackEntry ->
            val routeId = backStackEntry.arguments?.getString("routeId") ?: ""
            RouteScreen(
                navController,
                routeFirebaseViewModel,
                routeId,
                userProfileViewModel,
                routeCreationViewModel
            )
        }
        composable("FiltersScreen") {
            FiltersScreen(navController, filtersViewModel)
        }
    }
}