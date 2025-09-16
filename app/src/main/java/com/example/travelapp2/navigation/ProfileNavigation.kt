package com.example.travelapp2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.travelapp2.presentation.login.LoginScreen
import com.example.travelapp2.presentation.profile.createRoute.RouteCreationViewModel
import com.example.travelapp2.presentation.profile.createRoute.routeAttributes.RouteAttributesScreen
import com.example.travelapp2.presentation.profile.createRoute.routeDetails.RouteDetailsScreen
import com.example.travelapp2.presentation.profile.createRoute.routePoints.RoutePointsScreen
import com.example.travelapp2.presentation.profile.createTrip.TripCreationViewModel
import com.example.travelapp2.presentation.profile.createTrip.tripDetails.TripDetailsScreen
import com.example.travelapp2.presentation.profile.createTrip.tripPoints.TripPointsScreen
import com.example.travelapp2.presentation.profile.createTrip.tripPointsDetails.TripPointDetailsScreen
import com.example.travelapp2.presentation.profile.createTrip.tripPointsPreview.TripPointsPreviewScreen
import com.example.travelapp2.presentation.profile.profile.ProfileScreen
import com.example.travelapp2.presentation.profile.profile.UserProfileViewModel
import com.example.travelapp2.presentation.profile.profileInformation.ProfileInformationScreen
import com.example.travelapp2.presentation.profile.profileSub.ProfileSubScreen
import com.example.travelapp2.presentation.route.RouteScreen
import com.example.travelapp2.presentation.trip.TripScreen
import com.example.travelapp2.presentation.viewmodels.LocationPointFirebaseViewModel
import com.example.travelapp2.presentation.viewmodels.MapHolderViewModel
import com.example.travelapp2.presentation.viewmodels.RouteFirebaseViewModel
import com.example.travelapp2.presentation.viewmodels.TripFirebaseViewModel
import com.example.travelapp2.presentation.viewmodels.UserPreferencesViewModel

@Composable
fun Profile(
    routeFirebaseViewModel: RouteFirebaseViewModel,
    tripFirebaseViewModel: TripFirebaseViewModel,
    userProfileViewModel: UserProfileViewModel,
    mainNavController: NavHostController,
    routeCreationViewModel: RouteCreationViewModel,
    mapHolderViewModel: MapHolderViewModel,
    userPreferencesViewModel: UserPreferencesViewModel
) {
    val navController = rememberNavController()
    val tripCreationViewModel: TripCreationViewModel = hiltViewModel()
    val locationPointFirebaseViewModel: LocationPointFirebaseViewModel = hiltViewModel()

    NavHost(navController, startDestination = "Profile") {
        composable("Profile") {
            val currentUserId = userProfileViewModel.currentUser
            LaunchedEffect(Unit) {
                navController.navigate("Profile/$currentUserId") {
                    popUpTo("Profile") { inclusive = true }
                }
            }
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
        composable(route = "ProfileSubScreen/{userId}/{selectedTab}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val selectedTab = backStackEntry.arguments?.getString("selectedTab")?.toIntOrNull() ?: 0

            ProfileSubScreen(
                navController = navController,
                userProfileViewModel = userProfileViewModel,
                userId = userId,
                selectedTab = selectedTab
            )
        }
        composable(route = "ProfileInformationScreen/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ProfileInformationScreen(
                navController = navController,
                userProfileViewModel = userProfileViewModel,
                userId = userId,
                        mainNavController = mainNavController
            )
        }
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("TripScreen/{tripId}") { backStackEntry ->
            val tripId = backStackEntry.arguments?.getString("tripId") ?: ""
            TripScreen(navController, tripFirebaseViewModel, userProfileViewModel, tripId, tripCreationViewModel, locationPointFirebaseViewModel)
        }
        composable("RouteScreen/{routeId}") { backStackEntry ->
            val routeId = backStackEntry.arguments?.getString("routeId") ?: ""
            RouteScreen(navController, routeFirebaseViewModel, routeId, userProfileViewModel, routeCreationViewModel)
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
        composable("TripDetails") {
            TripDetailsScreen(navController, tripCreationViewModel)
        }
        composable("TripPoints") {
            TripPointsScreen(navController, locationPointFirebaseViewModel, mapHolderViewModel)
        }
        composable("TripPointsPreview") {
            TripPointsPreviewScreen(navController, tripCreationViewModel, locationPointFirebaseViewModel )
        }
        composable("TripPointDetails/{pointId}") { backStackEntry ->
            val pointId = backStackEntry.arguments?.getString("pointId") ?: ""
            TripPointDetailsScreen(navController, locationPointFirebaseViewModel, pointId)
        }
    }
}
