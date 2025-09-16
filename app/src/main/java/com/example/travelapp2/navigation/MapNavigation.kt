package com.example.travelapp2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.travelapp2.main.home.filters.FiltersScreen
import com.example.travelapp2.main.home.filters.FiltersViewModel
import com.example.travelapp2.presentation.map.MapScreen
import com.example.travelapp2.presentation.place.place2gis.GisPlaceScreen
import com.example.travelapp2.presentation.place.placegeoapify.GeoApifyPlaceScreen
import com.example.travelapp2.presentation.profile.createRoute.RouteCreationViewModel
import com.example.travelapp2.presentation.profile.createRoute.routeAttributes.RouteAttributesScreen
import com.example.travelapp2.presentation.profile.createRoute.routeDetails.RouteDetailsScreen
import com.example.travelapp2.presentation.profile.createRoute.routePoints.RoutePointsScreen
import com.example.travelapp2.presentation.profile.profile.UserProfileViewModel
import com.example.travelapp2.presentation.routes.RoutesSavedScreen
import com.example.travelapp2.presentation.viewmodels.CategorySearchViewModel
import com.example.travelapp2.presentation.viewmodels.GIS2ViewModel
import com.example.travelapp2.presentation.viewmodels.LocationViewModel
import com.example.travelapp2.presentation.viewmodels.MapHolderViewModel
import com.example.travelapp2.presentation.viewmodels.RouteFirebaseViewModel
import com.example.travelapp2.presentation.viewmodels.UserPreferencesViewModel

@Composable
fun Map(
    mainNavController: NavHostController,
    locationViewModel: LocationViewModel,
    mapViewModel: GIS2ViewModel,
    categorySearchViewModel: CategorySearchViewModel,
    mapHolderViewModel: MapHolderViewModel,
    userProfileViewModel: UserProfileViewModel,
    routeFirebaseViewModel: RouteFirebaseViewModel,
    userPreferencesViewModel: UserPreferencesViewModel,
    filtersViewModel: FiltersViewModel,
    routeCreationViewModel: RouteCreationViewModel
) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "MapScreen") {
        composable("MapScreen") {
            MapScreen(mainNavController, locationViewModel, mapViewModel, navController, categorySearchViewModel, mapHolderViewModel, routeCreationViewModel)
        }
        composable("GisPlaceScreen") {
            GisPlaceScreen(navController, mapViewModel, userProfileViewModel)
        }
        composable("GeoApifyPlaceScreen") {
            GeoApifyPlaceScreen(navController, categorySearchViewModel, userProfileViewModel)
        }
        composable("RoutesSavedScreen") {
            RoutesSavedScreen(mainNavController, navController, routeFirebaseViewModel, userPreferencesViewModel, filtersViewModel)
        }
        composable("FiltersScreen") {
            FiltersScreen(navController, filtersViewModel)
        }
        composable("RoutePoints") {
            RoutePointsScreen(navController, routeCreationViewModel, mapHolderViewModel)
        }
        composable("RouteAttributes") {
            RouteAttributesScreen(navController, routeCreationViewModel)
        }
        composable("RouteDetails") {
            RouteDetailsScreen(navController, routeCreationViewModel)
        }
    }
}