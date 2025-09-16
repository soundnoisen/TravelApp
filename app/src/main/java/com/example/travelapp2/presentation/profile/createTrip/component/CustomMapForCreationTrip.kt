package com.example.travelapp2.presentation.profile.createTrip.component

import androidx.compose.runtime.Composable
import com.example.travelapp2.data.models.LocationPointFirebase
import com.example.travelapp2.data.models.RoutePoint
import com.example.travelapp2.presentation.profile.component.CustomMapInternal
import com.example.travelapp2.presentation.viewmodels.MapHolderViewModel
import org.osmdroid.util.GeoPoint
import java.util.UUID


@Composable
fun CustomMapForCreationTrip(
    route: List<LocationPointFirebase>,
    mapHolderViewModel: MapHolderViewModel,
    onRouteUpdated: (List<LocationPointFirebase>) -> Unit
) {
    val parsedRoute = route.map { point ->
        RoutePoint(
            geoPoint = GeoPoint(point.latitude, point.longitude),
            name = point.name,
            address = point.address,
            city = point.city
        )
    }

    CustomMapInternal(parsedRoute, mapHolderViewModel) { updatedRoute ->
        val newMappedRoute = updatedRoute.mapIndexed { index, point ->
            LocationPointFirebase(
                id = route.getOrNull(index)?.id ?: UUID.randomUUID().toString(),
                name = point.name,
                city = point.city,
                description = route.getOrNull(index)?.description ?: "",
                date = route.getOrNull(index)?.date ?: "",
                photos = route.getOrNull(index)?.photos ?: emptyList(),
                latitude = point.geoPoint.latitude,
                longitude = point.geoPoint.longitude,
                address = point.address,
                index = index
            )
        }
        onRouteUpdated(newMappedRoute)
    }
}
