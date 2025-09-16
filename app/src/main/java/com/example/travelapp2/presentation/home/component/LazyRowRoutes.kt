package com.example.travelapp2.presentation.home.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.travelapp2.presentation.viewmodels.UserPreferencesViewModel
import com.example.travelapp2.data.models.RouteFirebase
import com.example.travelapp2.presentation.common.RouteCard
import com.example.travelapp2.presentation.viewmodels.RouteFirebaseViewModel


@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun LazyRowRoutes(
    routes: List<RouteFirebase>,
    navController: NavHostController,
    userPreferencesViewModel: UserPreferencesViewModel,
    routeFirebaseViewModel: RouteFirebaseViewModel,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)

    ) {
        itemsIndexed(routes) { _, route ->
            RouteCard(
                route = route,
                onClick = { navController.navigate("RouteScreen/${route.id}")
                    routeFirebaseViewModel.onRouteViewed(route)
                    userPreferencesViewModel.trackAndSaveRouteView(route)
                },
                modifier = Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp - 16.dp*2)
                    .padding(vertical = 8.dp)
                    .animateItem(fadeInSpec = null, fadeOutSpec = null)
            )
        }
    }
}