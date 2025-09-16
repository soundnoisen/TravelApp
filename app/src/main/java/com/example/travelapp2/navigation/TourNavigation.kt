package com.example.travelapp2.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.travelapp2.presentation.place.place2gis.GisPlaceScreen
import com.example.travelapp2.presentation.place.placegeoapify.GeoApifyPlaceScreen
import com.example.travelapp2.presentation.profile.profile.ProfileScreen
import com.example.travelapp2.presentation.profile.profile.UserProfileViewModel
import com.example.travelapp2.presentation.tour.ChatViewModel
import com.example.travelapp2.presentation.tour.PlacePickerScreen
import com.example.travelapp2.presentation.tour.RoutePlacePickerScreen
import com.example.travelapp2.presentation.tour.TourScreen
import com.example.travelapp2.presentation.tour.TourAdminScreen
import com.example.travelapp2.presentation.tourCreate.TourCreationScreen
import com.example.travelapp2.presentation.tourCreate.TourCreationViewModel
import com.example.travelapp2.presentation.tours.ToursScreen
import com.example.travelapp2.presentation.viewmodels.CategorySearchViewModel
import com.example.travelapp2.presentation.viewmodels.GIS2ViewModel
import com.example.travelapp2.presentation.viewmodels.RouteFirebaseViewModel
import com.example.travelapp2.presentation.viewmodels.ToursViewModel
import com.example.travelapp2.presentation.viewmodels.TripFirebaseViewModel
import com.example.travelapp2.presentation.viewmodels.UserPreferencesViewModel


@Composable
fun Tours(
    toursViewModel: ToursViewModel,
    userProfileViewModel: UserProfileViewModel,
    chatViewModel: ChatViewModel,
    tripFirebaseViewModel: TripFirebaseViewModel,
    routeFirebaseViewModel: RouteFirebaseViewModel,
    mapViewModel: GIS2ViewModel,
    categorySearchViewModel: CategorySearchViewModel,
    userPreferencesViewModel: UserPreferencesViewModel
) {
    val tourCreationViewModel: TourCreationViewModel = hiltViewModel()
    val navController = rememberNavController()

    NavHost(navController, startDestination = "ToursScreen") {
        composable("ToursScreen") {
            ToursScreen(navController, toursViewModel)
        }
        composable("TourCreation") {
            TourCreationScreen(navController, tourCreationViewModel)
        }
        composable("TourScreen/{tourId}") { backStackEntry ->
            val tourId = backStackEntry.arguments?.getString("tourId") ?: ""
            TourScreen(navController, toursViewModel, tourId, userProfileViewModel, chatViewModel, tourCreationViewModel, mapViewModel, categorySearchViewModel)
        }
        composable("placePickerScreen/{key}/{tourId}") { backStackEntry ->
            val key = backStackEntry.arguments?.getString("key") ?: ""
            val tourId = backStackEntry.arguments?.getString("tourId") ?: ""
            PlacePickerScreen(key, tourId, toursViewModel, userProfileViewModel, navController ,mapViewModel, categorySearchViewModel)
        }
        composable("GisPlaceScreen") {
            GisPlaceScreen(navController, mapViewModel, userProfileViewModel)
        }
        composable("GeoApifyPlaceScreen") {
            GeoApifyPlaceScreen(navController, categorySearchViewModel, userProfileViewModel)
        }
        composable("RoutePlacePickerScreen/{tourId}") { backStackEntry ->
            val tourId = backStackEntry.arguments?.getString("tourId") ?: ""
            RoutePlacePickerScreen(
                tourId = tourId,
                toursViewModel = toursViewModel,
                navController = navController,
                mapViewModel, categorySearchViewModel)
        }
        composable("GisPlaceScreen") {
            GisPlaceScreen(navController, mapViewModel, userProfileViewModel)
        }
        composable("GeoApifyPlaceScreen") {
            GeoApifyPlaceScreen(navController, categorySearchViewModel, userProfileViewModel)
        }
        composable("TourAdminScreen/{tourId}") { backStackEntry ->
            val tourId = backStackEntry.arguments?.getString("tourId") ?: ""
            TourAdminScreen(
                navController = navController,
                tourId = tourId,
                toursViewModel = toursViewModel,
                tourCreationViewModel = tourCreationViewModel)
        }
        composable(route = "Profile/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ProfileScreen(
                navController = navController,
                userProfileViewModel = userProfileViewModel,
                tripFirebaseViewModel = tripFirebaseViewModel,
                routeFirebaseViewModel = routeFirebaseViewModel,
                userId = userId,
                userPreferencesViewModel = userPreferencesViewModel
            )
        }
    }
}




