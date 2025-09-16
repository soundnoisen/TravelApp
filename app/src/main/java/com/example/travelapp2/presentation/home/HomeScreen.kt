package com.example.travelapp2.presentation.home

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.travelapp2.R
import com.example.travelapp2.presentation.common.CustomText
import com.example.travelapp2.presentation.home.component.HomeHeader
import com.example.travelapp2.presentation.common.SearchPlaceField
import com.example.travelapp2.presentation.common.TitleRow
import com.example.travelapp2.presentation.viewmodels.LocationViewModel
import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.testTag
import com.example.travelapp2.presentation.viewmodels.UserPreferencesViewModel
import com.example.travelapp2.presentation.viewmodels.UserRecommendationViewModel
import com.example.travelapp2.presentation.home.component.LazyRowRoutes
import com.example.travelapp2.presentation.viewmodels.CategorySearchViewModel
import com.example.travelapp2.data.models.Coordinates
import com.example.travelapp2.presentation.viewmodels.GIS2ViewModel
import com.example.travelapp2.presentation.viewmodels.RouteFirebaseViewModel
import com.example.travelapp2.navigation.NavigationMainBar
import com.example.travelapp2.presentation.home.component.LazyRowCategories
import com.example.travelapp2.presentation.home.models.categoryList


@Composable
fun HomeScreen(
    locationViewModel: LocationViewModel,
    mapViewModel: GIS2ViewModel,
    navController: NavHostController,
    mainNavController: NavHostController,
    categorySearchViewModel: CategorySearchViewModel,
    routeFirebaseViewModel: RouteFirebaseViewModel,
    userRecommendationViewModel: UserRecommendationViewModel,
    userPreferencesViewModel: UserPreferencesViewModel
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        routeFirebaseViewModel.loadInterestingRoutes()
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            locationViewModel.getLocation(context)
        }
    }

    val routes by routeFirebaseViewModel.interestingRoutes.observeAsState(emptyList())
    var searchPlace by remember { mutableStateOf("") }
    var categoryList by remember { mutableStateOf(categoryList()) }
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    val routesRecommendation by userRecommendationViewModel.routesRecommendation.observeAsState(emptyList())

    val city by locationViewModel.city.collectAsState(initial = "Местоположение не определено")
    val cityId2Gis by locationViewModel.cityId2Gis.collectAsState()
    val coordinatesState by locationViewModel.coordinates.collectAsState()

    fun navigateToMap() {
        mainNavController.navigate(NavigationMainBar.Map.route) {
            mainNavController.graph.startDestinationRoute?.let { route ->
                popUpTo(route) { saveState = true }
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted)
                locationViewModel.getLocation(context)
            else
                Toast.makeText(context, "Разрешение на доступ к местоположению не предоставлено", Toast.LENGTH_SHORT).show()
        }
    )

    Column(modifier = Modifier.background(colorResource(id = R.color.background))) {
        HomeHeader(
            locationName = city,
            onLocationClick = {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationViewModel.getLocation(context)
                } else {
                    locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            },
            modifier = Modifier.padding(top = 40.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.background))
                .verticalScroll(rememberScrollState())
        ) {
            Column {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomText("Начнем путешествие!", 20, R.font.manrope_semibold, 26)
                    Spacer(modifier = Modifier.height(8.dp))
                    SearchPlaceField(
                        text = searchPlace,
                        onTextChange = { newText -> searchPlace = newText },
                        onClickSearchPlace = {
                            if (cityId2Gis != null) {
                                mapViewModel.updateQuery(searchPlace)
                                mapViewModel.updateCityId(cityId2Gis!!)
                                navigateToMap()
                            } else {
                                if (searchPlace.isEmpty()) {
                                    Toast.makeText(context, "Введите интересующее место в поиске", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier.testTag("search_field"),
                        modifier2 = Modifier.testTag("search_button")
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomText("Места рядом", 18, R.font.manrope_semibold, 26)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                LazyRowCategories(categoryList, selectedCategoryIndex, userPreferencesViewModel, Modifier.testTag("category_item")) { index, category ->
                    categoryList = categoryList.toMutableList().apply {
                        val selectedCategory = removeAt(index)
                        add(0, selectedCategory)
                    }
                    selectedCategoryIndex = 0

                    coordinatesState?.let { (lat, lon) ->
                        categorySearchViewModel.updateCoordinates(Coordinates(lat, lon))
                        categorySearchViewModel.updateCategory(category)
                        categorySearchViewModel.updateLimit(500)
                        categorySearchViewModel.updateRadius(5000)
                        navigateToMap()
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                TitleRow("Рекомендуемые маршруты") { navController.navigate("RoutesRecommendation") }
                Spacer(modifier = Modifier.height(8.dp))
                LazyRowRoutes(routesRecommendation, navController, userPreferencesViewModel, routeFirebaseViewModel)

                Spacer(modifier = Modifier.height(16.dp))
                TitleRow("Интересные маршруты") { navController.navigate("RoutesScreen") }
                Spacer(modifier = Modifier.height(8.dp))
                LazyRowRoutes(routes, navController, userPreferencesViewModel, routeFirebaseViewModel)

                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}
