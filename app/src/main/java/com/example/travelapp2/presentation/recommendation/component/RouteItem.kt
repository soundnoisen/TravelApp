package com.example.travelapp2.presentation.recommendation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.travelapp2.R
import com.example.travelapp2.data.network.Route
import com.example.travelapp2.presentation.common.CustomButton
import com.example.travelapp2.presentation.viewmodels.RouteFirebaseViewModel


@Composable
fun RouteItem(
    index: Int,
    route: Route,
    city: String,
    navHostController: NavHostController,
    routeFirebaseViewModel: RouteFirebaseViewModel,
    saveRoute: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Маршрут №${index + 1}",
                fontSize = 16.sp,
                fontFamily = Font(R.font.manrope_semibold).toFontFamily()
            )

            Spacer(modifier = Modifier.height(8.dp))

            DataRouteItem(R.drawable.ic_tour_location, "Дистанция: ${"%.1f".format(route.total_distance/1000)} км", 24)
            Spacer(modifier = Modifier.height(8.dp))
            DataRouteItem(R.drawable.ic_route, "Путь между точками: ${formatDuration(route.total_duration.toInt())}", 24)
            Spacer(modifier = Modifier.height(8.dp))
            DataRouteItem(R.drawable.ic_time, "Время маршрута: ${formatDuration(route.total_visit_time.toInt())}", 20)

            Spacer(modifier = Modifier.height(8.dp))
            Text("Места:",
                fontSize = 16.sp,
                fontFamily = Font(R.font.manrope_semibold).toFontFamily()
            )

            Spacer(modifier = Modifier.height(4.dp))

            RecRoute(
                route = route,
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(Modifier.fillMaxWidth()) {
                CustomButton(onClick = saveRoute,
                    buttonText = "Сохранить", modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                CustomButton(onClick = {
                    routeFirebaseViewModel.updateRouteGeneratedByApp(city, route)
                    routeFirebaseViewModel.updatePlacesGeneratedByApp(route.places)
                    navHostController.navigate("GeneratedRouteByAppScreen")
                }, buttonText = "Подробнее", modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun DataRouteItem(icon: Int, text: String, size: Int) {
    Row(Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.size(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon), contentDescription = null, modifier = Modifier.size(size.dp)

            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontFamily = Font(R.font.inter_regular).toFontFamily()
        )
    }
}

fun formatDuration(minutes: Int): String {
    val hours = minutes / 60
    val remainingMinutes = minutes % 60

    return when {
        hours > 0 && remainingMinutes > 0 -> "${hours} ч. ${remainingMinutes} мин"
        hours > 0 -> "${hours} ч"
        else -> "${minutes} мин"
    }
}