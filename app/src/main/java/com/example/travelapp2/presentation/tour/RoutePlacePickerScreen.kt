package com.example.travelapp2.presentation.tour

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.travelapp2.R
import com.example.travelapp2.presentation.profile.createRoute.component.Header
import com.example.travelapp2.presentation.viewmodels.CategorySearchViewModel
import com.example.travelapp2.presentation.viewmodels.GIS2ViewModel
import com.example.travelapp2.presentation.viewmodels.ToursViewModel

@Composable
fun RoutePlacePickerScreen(
    tourId: String,
    toursViewModel: ToursViewModel,
    navController: NavHostController,
    mapViewModel: GIS2ViewModel,
    categorySearchViewModel: CategorySearchViewModel
) {

    LaunchedEffect(tourId) {
        toursViewModel.listenToTourById(tourId)
    }


    val categoryNames = mapOf(
        "attractions" to "Достопримечательности",
        "nature" to "Природа и прогулки",
        "cultural" to "Культурные места",
        "restaurants" to "Рестораны и кафе",
    )
    

    val placesByCategory by toursViewModel.placesByCategory.observeAsState(emptyMap())
    val selectedCategory = remember { mutableStateOf<String?>(null) }
    val selectedPlaceIds = remember { mutableStateOf<Set<String>>(emptySet()) }

    val categories = placesByCategory.keys.toList()
    val currentPlaces = placesByCategory[selectedCategory.value] ?: emptyList()

    LaunchedEffect(placesByCategory) {
        if (selectedCategory.value == null && categories.isNotEmpty()) {
            selectedCategory.value = categories.first()
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.background))) {
        Spacer(modifier = Modifier.height(40.dp))
        Header(
            title = "Выберите места",
            startIcon = painterResource(id = R.drawable.ic_back3),
            startIconAction = { navController.navigateUp() },
            endIcon = painterResource(id = R.drawable.ic_next3),
            endIconAction = {
                if (selectedPlaceIds.value.isNotEmpty() && selectedCategory.value != null) {
                    toursViewModel.addPlacesToRouteFromCategory(
                        categoryKey = selectedCategory.value!!,
                        placeIds = selectedPlaceIds.value,
                        placesByCategory = placesByCategory
                    ) {
                        navController.navigateUp()
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Spacer(modifier = Modifier.width(8.dp))
            }

            items(categories) { category ->
                val isSelected = category == selectedCategory.value
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) colorResource(id = R.color.main) else colorResource(id = R.color.next))
                        .clickable { selectedCategory.value = category }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = categoryNames[category] ?: category,
                        color = if (isSelected) Color.White else Color.Black,
                        fontSize = 14.sp
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.background))
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(currentPlaces) { place ->
                PlaceCard(
                    place = place,
                    isSelected = selectedPlaceIds.value.contains(place.id),
                    onClick = {
                        if (place.fromApi == "2gis") {
                            mapViewModel.updatePlace(place)
                            navController.navigate("GisPlaceScreen")
                        }
                        if (place.fromApi == "geoapify") {
                            categorySearchViewModel.updatePlace(place)
                            navController.navigate("GeoApifyPlaceScreen")
                        }
                    },
                    onLongClick = {
                        selectedPlaceIds.value = selectedPlaceIds.value.toMutableSet().apply {
                            if (contains(place.id)) remove(place.id) else add(place.id)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
