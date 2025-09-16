package com.example.travelapp2.navigation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.travelapp2.R
import com.example.travelapp2.main.home.filters.FiltersViewModel
import com.example.travelapp2.presentation.profile.createRoute.RouteCreationViewModel
import com.example.travelapp2.presentation.profile.profile.UserProfileViewModel
import com.example.travelapp2.presentation.recommendation.GenerateRouteFiltersScreen
import com.example.travelapp2.presentation.viewmodels.UserRecommendationViewModel
import com.example.travelapp2.presentation.recommendation.RecommendationsScreen
import com.example.travelapp2.presentation.route.GeneratedRouteByAppScreen
import com.example.travelapp2.presentation.route.RouteScreen
import com.example.travelapp2.presentation.tour.ChatViewModel
import com.example.travelapp2.presentation.viewmodels.CategorySearchViewModel
import com.example.travelapp2.presentation.viewmodels.GIS2ViewModel
import com.example.travelapp2.presentation.viewmodels.LocationViewModel
import com.example.travelapp2.presentation.viewmodels.MapHolderViewModel
import com.example.travelapp2.presentation.viewmodels.RouteFirebaseViewModel
import com.example.travelapp2.presentation.viewmodels.ToursViewModel
import com.example.travelapp2.presentation.viewmodels.TripFirebaseViewModel
import com.example.travelapp2.presentation.viewmodels.UserPreferencesViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.cloudinary.json.JSONObject
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import java.io.IOException

@Composable
fun Main(mainNavController: NavHostController) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                Navigation(navController, mainNavController)
            } },
        backgroundColor = colorResource(R.color.white)
    )
}



sealed class NavigationMainBar(var route: String, var icon: Int, var title: String) {
    data object Home : NavigationMainBar("home", R.drawable.ic_menu_home, "Home")
    data object Map : NavigationMainBar("map", R.drawable.ic_menu_map, "Map")
    data object Generate : NavigationMainBar("generate", R.drawable.im_generate, "Generate")
    data object Tours : NavigationMainBar("tours", R.drawable.ic_chat, "Tours")
    data object Profile : NavigationMainBar("profile", R.drawable.ic_menu_profile, "Profile")
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationMainBar.Home,
        NavigationMainBar.Map,
        NavigationMainBar.Generate,
        NavigationMainBar.Tours,
        NavigationMainBar.Profile
    )
    BottomNavigation(
        backgroundColor = colorResource(id = R.color.white),
        contentColor = Color.White,
        modifier = Modifier
            .height(56.dp)

    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        Row(Modifier.padding(horizontal = 24.dp)) {
            items.forEach { item ->
                BottomNavigationItem(
                    icon = {
                        if (item.route == "generate")
                            Image(painter = painterResource(id = item.icon), contentDescription = "", modifier = Modifier.size(50.dp, 38.dp))
                        else
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = item.title,
                                modifier = Modifier.size(50.dp, 24.dp),
                                tint = if (currentRoute == item.route) colorResource(id = R.color.main) else colorResource(
                                    id = R.color.hint
                                )
                            )
                    },
                    selectedContentColor = colorResource(id = R.color.main),
                    unselectedContentColor = colorResource(id = R.color.hint),
                    alwaysShowLabel = true,

                    selected = currentRoute == item.route,
                    modifier = Modifier.testTag(item.title),
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun Navigation(navController: NavHostController, mainNavController: NavHostController) {
    val userRecommendationViewModel: UserRecommendationViewModel = hiltViewModel()
    val userProfileViewModel: UserProfileViewModel = hiltViewModel()
    val categorySearchViewModel: CategorySearchViewModel = hiltViewModel()
    val routeFirebaseViewModel: RouteFirebaseViewModel = hiltViewModel()
    val tripFirebaseViewModel: TripFirebaseViewModel = hiltViewModel()
    val userPreferencesViewModel: UserPreferencesViewModel = hiltViewModel()
    val locationViewModel: LocationViewModel = hiltViewModel()
    val mapViewModel: GIS2ViewModel = hiltViewModel()
    val toursViewModel: ToursViewModel = hiltViewModel()
    val chatViewModel: ChatViewModel = hiltViewModel()
    val routeCreationViewModel: RouteCreationViewModel = hiltViewModel()
    val mapHolderViewModel: MapHolderViewModel = hiltViewModel()
    val filtersViewModel: FiltersViewModel = hiltViewModel()

    NavHost(navController, startDestination = NavigationMainBar.Home.route) {

        composable(NavigationMainBar.Home.route) {
            Home(locationViewModel = locationViewModel, mapViewModel, navController, categorySearchViewModel, routeFirebaseViewModel, userRecommendationViewModel, userPreferencesViewModel, userProfileViewModel, routeCreationViewModel, filtersViewModel)
        }
        composable(NavigationMainBar.Map.route) {
            Map(navController, locationViewModel, mapViewModel, categorySearchViewModel, mapHolderViewModel, userProfileViewModel, routeFirebaseViewModel, userPreferencesViewModel, filtersViewModel, routeCreationViewModel)
        }


        composable(NavigationMainBar.Generate.route) {
            RecommendationsScreen(navController, userRecommendationViewModel, locationViewModel, routeCreationViewModel, routeFirebaseViewModel)
        }
        composable("GenerateRouteFilters") {
            GenerateRouteFiltersScreen(navController, userRecommendationViewModel, locationViewModel)
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
        composable("GeneratedRouteByAppScreen") {
            GeneratedRouteByAppScreen(navController, routeFirebaseViewModel, "", userProfileViewModel)
        }
        composable(NavigationMainBar.Tours.route) {
            Tours(toursViewModel, userProfileViewModel, chatViewModel, tripFirebaseViewModel, routeFirebaseViewModel, mapViewModel, categorySearchViewModel, userPreferencesViewModel)
        }
        composable(NavigationMainBar.Profile.route) {
            Profile(routeFirebaseViewModel, tripFirebaseViewModel, userProfileViewModel, mainNavController, routeCreationViewModel, mapHolderViewModel, userPreferencesViewModel)
        }
    }
}

