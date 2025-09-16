package com.example.travelapp2.presentation.profile.createRoute.routePoints

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.travelapp2.R
import com.example.travelapp2.data.models.RouteFirebase
import com.example.travelapp2.presentation.profile.createRoute.component.Header
import com.example.travelapp2.presentation.profile.createRoute.component.showToast
import com.example.travelapp2.presentation.profile.createRoute.routePoints.component.CustomMapForCreationRoute
import com.example.travelapp2.presentation.profile.createRoute.RouteCreationViewModel
import com.example.travelapp2.presentation.viewmodels.MapHolderViewModel

@Composable
fun RoutePointsScreen(navController: NavHostController, routeCreationViewModel: RouteCreationViewModel, mapHolderViewModel: MapHolderViewModel) {
    val context = LocalContext.current
    val routeData by routeCreationViewModel.routeData.observeAsState(RouteFirebase())
    var routePoints by remember { mutableStateOf(routeData.route) }


    fun proceedToDescription() {
        when {
            routePoints.isEmpty() -> showToast(context, "Укажите точки маршрута")
            routePoints.size < 3 -> showToast(context, "Маршрут должен иметь более 2-х точек")
            else -> {
                routeCreationViewModel.setRoutePoints(routePoints)
                navController.navigate("RouteAttributes")
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
                    endIconAction = { proceedToDescription() }
                )

            Box(Modifier.padding(top = 10.dp)) {
                CustomMapForCreationRoute(route = routePoints, mapHolderViewModel, onRouteUpdated = { updatedRoute ->
                    routePoints = updatedRoute
                })
            }
        }
    }
}
