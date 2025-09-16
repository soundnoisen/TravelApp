package com.example.travelapp2.presentation.profile.createTrip.tripPoints

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.travelapp2.R
import com.example.travelapp2.presentation.profile.createRoute.component.Header
import com.example.travelapp2.presentation.profile.createRoute.component.showToast
import com.example.travelapp2.presentation.profile.createTrip.component.CustomMapForCreationTrip
import com.example.travelapp2.presentation.viewmodels.LocationPointFirebaseViewModel
import com.example.travelapp2.presentation.viewmodels.MapHolderViewModel


@Composable
fun TripPointsScreen(
    navController: NavHostController,
    locationPointFirebaseViewModel: LocationPointFirebaseViewModel,
    mapHolderViewModel: MapHolderViewModel,
)
{
    val context = LocalContext.current

    val locations by locationPointFirebaseViewModel.locations.observeAsState(emptyList())

    fun onSavePressed() {
        when {
            locations.isEmpty() -> showToast(context, "Укажите точки маршрута")
            locations.size < 3 -> showToast(context, "Маршрут должен иметь более 2-х точек")
            else -> {
                Log.i("CreationLoc", "Длина списка точек: ${locations.size}")
                navController.navigate("TripPointsPreview")
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
    ) {
        Column() {
            Spacer(modifier = Modifier.padding(top = 40.dp))

                Header(
                    title = "Маршрут",
                    startIcon = painterResource(id = R.drawable.ic_back3),
                    startIconAction = { navController.navigateUp() },
                    endIcon = painterResource(id = R.drawable.ic_next3),
                    endIconAction = { onSavePressed() })

            Box(
                Modifier
                    .fillMaxSize()
                    .clipToBounds()) {
                CustomMapForCreationTrip(route = locations, mapHolderViewModel, onRouteUpdated = { updatedTrip ->
                    locationPointFirebaseViewModel.updateLocations(updatedTrip)
                })
            }
        }
    }
}