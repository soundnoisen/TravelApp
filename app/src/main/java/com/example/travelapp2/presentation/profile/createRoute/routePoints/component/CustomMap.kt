package com.example.travelapp2.presentation.profile.createRoute.routePoints.component

import androidx.compose.runtime.Composable
import com.example.travelapp2.data.models.RoutePoint
import com.example.travelapp2.presentation.profile.component.CustomMapInternal
import com.example.travelapp2.presentation.viewmodels.MapHolderViewModel
import org.osmdroid.util.GeoPoint

@Composable
fun CustomMapForCreationRoute(
    route: List<Map<String, Any>>,
    mapHolderViewModel: MapHolderViewModel,
    onRouteUpdated: (List<Map<String, Any>>) -> Unit,
) {
    val parsedRoute = route.map { point ->
        RoutePoint(
            geoPoint = GeoPoint(point["latitude"] as Double, point["longitude"] as Double),
            name = point["name"] as? String ?: "",
            address = point["address"] as? String ?: "Неизвестный адрес",
            city = point["city"] as? String ?: "Неизвестный город"
        )
    }

    CustomMapInternal(parsedRoute, mapHolderViewModel) { updatedRoute ->
        val newMappedRoute = updatedRoute.map { point ->
            mapOf(
                "latitude" to point.geoPoint.latitude,
                "longitude" to point.geoPoint.longitude,
                "name" to point.name,
                "address" to point.address,
                "city" to point.city
            )
        }
        onRouteUpdated(newMappedRoute)
    }
}
