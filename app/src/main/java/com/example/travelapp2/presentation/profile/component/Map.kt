package com.example.travelapp2.presentation.profile.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.travelapp2.R
import com.example.travelapp2.data.models.RoutePoint
import com.example.travelapp2.presentation.common.SearchPlaceField
import com.example.travelapp2.presentation.map.component.LocationPickerDialog
import com.example.travelapp2.presentation.map.component.RoutePointRow
import com.example.travelapp2.presentation.map.component.createCustomMarkerDrawable
import com.example.travelapp2.presentation.map.component.rememberMapViewWithLifecycle
import com.example.travelapp2.presentation.util.getRoutesBetweenPoints
import com.example.travelapp2.presentation.viewmodels.MapHolderViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline


@Composable
fun CustomMapInternal(
    route: List<RoutePoint>,
    mapHolderViewModel: MapHolderViewModel,
    onRouteUpdated: (List<RoutePoint>) -> Unit,
) {
    var searchPlace by remember { mutableStateOf("") }
    var showLocationPicker by remember { mutableStateOf(false) }

    val mapView = rememberMapViewWithLifecycle()

    LaunchedEffect(route) {
        mapView.overlays.clear()

        if (route.isNotEmpty()) {
            if (route.size > 1) {
                val geoPoints = route.map { it.geoPoint }
                val route = getRoutesBetweenPoints(geoPoints)

                route?.let { route ->
                    val polyline = Polyline().apply {
                        route.forEach { point ->
                            this.addPoint(point)
                        }
                        color = ContextCompat.getColor(mapView.context, R.color.main)
                        width = 6f
                    }
                    mapView.overlays.add(polyline)
                }
            }

            route.forEachIndexed { index, point ->
                val marker = Marker(mapView).apply {
                    position = point.geoPoint
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                    icon = createCustomMarkerDrawable(mapView.context, index + 1)
                }
                mapView.overlays.add(marker)
            }
            mapView.controller.setCenter(route.last().geoPoint)
        }
        mapView.invalidate()

    }

    if (showLocationPicker) {
        LocationPickerDialog(
            onLocationSelected = { geoPoint, name, address, city ->


                val newPoint = RoutePoint(
                    geoPoint,
                    name ?: "",
                    if (address != null) "$address" else "Местоположение: ${geoPoint.latitude}, ${geoPoint.longitude}",
                    if (city.isNullOrEmpty()) "" else city
                )
                Log.i("ZXC", "Добавляется точка: name=${newPoint.name}, address=${newPoint.address}, city=${newPoint.city}")

                onRouteUpdated(route + newPoint)

                showLocationPicker = false
            },
            onDismiss = { showLocationPicker = false },
            lastPoint = route.lastOrNull(),
        )
    }


    Box(modifier = Modifier
        .fillMaxSize()
        .clipToBounds())
    {
        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = { context ->
                mapView.apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                    controller.setZoom(10.0)
                    controller.setCenter(GeoPoint(55.796129, 49.106414))
                    setBuiltInZoomControls(false)
                }
            }
        )
    }

    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column {
            Box(modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(colorResource(id = R.color.background))
                .padding(16.dp)
            ) {
                Column {
                    SearchPlaceField(
                        text = searchPlace,
                        onTextChange = { newText -> searchPlace = newText },
                        onClickSearchPlace = {}
                    )
                }
            }
            Box(modifier = Modifier
                .background(colorResource(id = R.color.background))
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .heightIn(max = 156.dp)
                .shadow(
                    16.dp,
                    shape = RoundedCornerShape(10.dp),
                    spotColor = Color.Black.copy(alpha = 0.06f)
                )
                .background(colorResource(id = R.color.background))
                .background(Color.White, shape = RoundedCornerShape(10.dp))
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()) // Включаем вертикальный скролл
                        .padding(end = 8.dp) // Внутренние отступы
                ) {
                    route.forEachIndexed { index, point ->

                        val text = point.name.ifEmpty {
                            point.address
                        }

                        RoutePointRow(
                            index = index,
                            point = text,
                            onTextChange = { newText ->
                                val updatedPoints = route.toMutableList().apply {
                                    set(index, point.copy(address = newText))
                                }
                                onRouteUpdated(updatedPoints)
                            },
                            onDelete = {
                                if (route.size > 1) {
                                    val updatedPoints = route.toMutableList().apply {
                                        removeAt(index)
                                    }
                                    onRouteUpdated(updatedPoints)
                                }
                            },
                            onMapClick = { geoPoint, address ->
                                val updatedPoints = route.toMutableList().apply {
                                    set(index, RoutePoint(geoPoint, if (address != null) "$address" else "Местоположение: ${geoPoint.latitude}, ${geoPoint.longitude}"))
                                }
                                onRouteUpdated(updatedPoints)
                            }
                        )
                    }
                    IconButton(
                        onClick = {
                            showLocationPicker = true
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(id = R.drawable.ic_add_point),
                            contentDescription = null,
                            tint = colorResource(id = R.color.main)
                        )
                    }
                }
            }
        }
    }
}