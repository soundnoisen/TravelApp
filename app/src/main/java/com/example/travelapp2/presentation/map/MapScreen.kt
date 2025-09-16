package com.example.travelapp2.presentation.map


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.osmdroid.views.overlay.Marker
import org.osmdroid.util.GeoPoint
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.travelapp2.R
import com.example.travelapp2.data.models.RoutePoint
import com.example.travelapp2.presentation.common.SearchPlaceField
import com.example.travelapp2.presentation.viewmodels.CategorySearchViewModel
import com.example.travelapp2.presentation.viewmodels.GIS2ViewModel
import com.example.travelapp2.data.models.Place
import com.example.travelapp2.presentation.map.component.CustomButton
import com.example.travelapp2.presentation.map.component.IconButtonCustom
import com.example.travelapp2.presentation.map.component.LocationPickerDialog
import com.example.travelapp2.presentation.map.component.RoutePointRow
import com.example.travelapp2.presentation.map.component.createCustomMarkerDrawable
import com.example.travelapp2.presentation.profile.createRoute.RouteCreationViewModel
import com.example.travelapp2.presentation.util.getRoutesBetweenPoints
import com.example.travelapp2.presentation.viewmodels.LocationViewModel
import com.example.travelapp2.presentation.viewmodels.MapHolderViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline


@Composable
fun MapScreen(
    mainNavController: NavHostController,
    locationViewModel: LocationViewModel,
    mapViewModel: GIS2ViewModel,
    navController: NavHostController,
    categorySearchViewModel: CategorySearchViewModel,
    mapHolderViewModel: MapHolderViewModel,
    routeCreationViewModel: RouteCreationViewModel
) {
    val context = LocalContext.current

    val places by mapViewModel.places.collectAsState()
    val coordinates by locationViewModel.coordinates.collectAsState()

    val categoryPlaces by categorySearchViewModel.places.collectAsState()
    val cityId2Gis by locationViewModel.cityId2Gis.collectAsState()

    var searchPlace by remember { mutableStateOf("") }

    var isPlacesCategoriesEnabled by remember { mutableStateOf(false) }
    var isPlacesSearchEnabled by remember { mutableStateOf(false) }
    var selectedPlace by remember { mutableStateOf<Place?>(null) }
    var points by remember { mutableStateOf(mutableListOf<RoutePoint>()) }
    var routePoints by remember { mutableStateOf<List<GeoPoint>?>(null) }
    var isRoutingEnabled by remember { mutableStateOf(false) }
    var showLocationPicker by remember { mutableStateOf(false) }

    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            setBuiltInZoomControls(false)
            org.osmdroid.config.Configuration.getInstance().userAgentValue = context.packageName
            controller.setZoom(mapHolderViewModel.lastZoom ?: 10.0)
            controller.setCenter(mapHolderViewModel.lastCenter ?: GeoPoint(55.796129, 49.106414))
        }
    }

    LaunchedEffect(Unit) {
        mapView.onResume()
    }

    DisposableEffect(Unit) {
        onDispose {
            mapHolderViewModel.saveMapState(
                center = mapView.mapCenter as GeoPoint,
                zoom = mapView.zoomLevelDouble
            )
            mapView.onPause()
        }
    }


    LaunchedEffect(mapViewModel.query, mapViewModel.cityId) {
        if (mapViewModel.cityId.value.isNotEmpty() || mapViewModel.query.value.isNotEmpty()) {
            mapViewModel.searchPlaces()
            isPlacesSearchEnabled = !isPlacesSearchEnabled
        }
    }

    LaunchedEffect(categorySearchViewModel.category) {
        if (categorySearchViewModel.category.value.isNotEmpty() || categorySearchViewModel.coordinates.value != null) {
            categorySearchViewModel.searchPlacesByCategory()
            isPlacesCategoriesEnabled = !isPlacesCategoriesEnabled
        }
    }

    LaunchedEffect(
        places.hashCode(),
        categoryPlaces.hashCode(),
        points.hashCode(),
        isPlacesSearchEnabled,
        isRoutingEnabled,
        isPlacesCategoriesEnabled
    ) {
        mapView.overlays.clear()

        if (isPlacesSearchEnabled) {
            places.forEach { place ->
                if (place.lat != null && place.lon != null) {
                    val marker = Marker(mapView).apply {
                        position = GeoPoint(place.lat, place.lon)
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        title = place.name
                        snippet = place.address
                        icon = ContextCompat.getDrawable(context, R.drawable.ic_point_geo_location)
                        setOnMarkerClickListener { _, _ ->
                            val placeVal = mapViewModel.createPlaceFirebase(place)
                            mapViewModel.updatePlace(placeVal)
                            navController.navigate("GisPlaceScreen")
                            true
                        }
                    }
                    mapView.overlays.add(marker)
                }
            }
            places.firstOrNull()?.let {
                if (it.lat != null && it.lon != null) {
                    mapView.controller.setCenter(GeoPoint(it.lat, it.lon))
                    mapView.controller.setZoom(15)
                }
            }
        } else if (isPlacesCategoriesEnabled) {
            categoryPlaces.forEach { place ->
                if (place.lat != null && place.lon != null) {
                    val marker = Marker(mapView).apply {
                        position = GeoPoint(place.lat, place.lon)
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        title = place.name
                        snippet = place.address
                        icon = ContextCompat.getDrawable(context, R.drawable.ic_point_geo_location)
                        setOnMarkerClickListener { _, _ ->
                            val placeVal = categorySearchViewModel.createPlaceFirebase(place)
                            categorySearchViewModel.updatePlace(placeVal)
                            navController.navigate("GeoApifyPlaceScreen")
                            true
                        }
                    }
                    mapView.overlays.add(marker)
                }
            }
            categoryPlaces.firstOrNull()?.let {
                if (it.lat != null && it.lon != null) {
                    mapView.controller.setCenter(GeoPoint(it.lat, it.lon))
                    mapView.controller.setZoom(15)
                }
            }
        } else if (isRoutingEnabled) {
            mapViewModel.deleteQuery()
            mapViewModel.deletePlaces()

            if (points.isNotEmpty()) {
                if (points.size > 1) {
                    val geoPoints = points.map { it.geoPoint }
                    val route = getRoutesBetweenPoints(geoPoints)
                    routePoints = route

                    routePoints?.let { route ->
                        val polyline = Polyline().apply {
                            route.forEach { point -> addPoint(point) }
                            color = ContextCompat.getColor(mapView.context, R.color.main)
                            width = 6f
                        }
                        mapView.overlays.add(polyline)
                    }
                }

                points.forEachIndexed { index, point ->
                    val marker = Marker(mapView).apply {
                        position = point.geoPoint
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                        icon = createCustomMarkerDrawable(mapView.context, index + 1)
                    }
                    mapView.overlays.add(marker)
                }

                mapView.controller.setCenter(points.last().geoPoint)
            }
        }

        mapView.invalidate()
    }

    if (showLocationPicker) {
        LocationPickerDialog(
            onLocationSelected = { geoPoint, name, address, city ->
                points = points.toMutableList().apply {
                    add(RoutePoint(geoPoint, name ?: "", address ?: "", city ?: ""))
                }
                showLocationPicker = false
            },
            onDismiss = { showLocationPicker = false },
            lastPoint = points.lastOrNull(),
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize().testTag("search_button"),
            factory = { mapView }
        )
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        contentAlignment = Alignment.TopEnd) {
    }

    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                IconButtonCustom(id = R.drawable.ic_route) {
                    selectedPlace = null
                    isPlacesSearchEnabled = false
                    isPlacesCategoriesEnabled = false
                    isRoutingEnabled = !isRoutingEnabled
                    if (coordinates != null) {
                        val latitude = coordinates!!.first
                        val longitude = coordinates!!.second
                        val location = GeoPoint(latitude, longitude)
                        points = mutableListOf(RoutePoint(location, "Ваше местоположение"))
                        routePoints = listOf(location)
                    }
                    mapViewModel.deleteQuery()
                    mapViewModel.deletePlaces()
                }
                IconButtonCustom(id = R.drawable.ic_unsave) {
                    navController.navigate("RoutesSavedScreen")
                }
            }
            Spacer(modifier = Modifier.padding(top = 16.dp))
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
                        onClickSearchPlace = {
                            if (cityId2Gis != null) {
                                mapViewModel.updateQuery(searchPlace)
                                mapViewModel.updateCityId(cityId2Gis!!)
                                mapViewModel.searchPlaces()
                                isPlacesSearchEnabled = true
                            } else {
                                if (searchPlace.isEmpty()) {
                                    Toast.makeText(context, "Введите интересующее место в поиске", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }
            }
            if (isRoutingEnabled) {
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
                    .background(Color.White, shape = RoundedCornerShape(10.dp)),
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(end = 8.dp)
                    ) {
                        points.forEachIndexed { index, point ->
                            Log.i("ZXC", "1 "+point.name+ " "+point.address)


                            val text = point.name.ifEmpty {
                                point.address
                            }

                            RoutePointRow(
                                index = index,
                                point = text,
                                onTextChange = {
//                                    newText ->
//                                    points = points.toMutableList().apply {
//                                        set(index, point.copy(address = newText))
//                                    }
                                },
                                onDelete = {
                                    if (points.size > 1) {
                                        points = points.toMutableList().apply { removeAt(index) }
                                    }
                                },
                                onMapClick = { geoPoint, address ->
                                    points = points.toMutableList().apply {
                                        set(index, RoutePoint(geoPoint, if (address != null) "${index+1}. $address" else "Местоположение: ${geoPoint.latitude}, ${geoPoint.longitude}"))
                                    }
                                }
                            )
                        }
                        IconButton(onClick = { showLocationPicker = true }
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
            if (isRoutingEnabled && points.size >= 2) {

                Box(
                    modifier = Modifier

                        .fillMaxWidth()
                        .background(colorResource(id = R.color.background))
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CustomButton(
                            onClick = {
                                val route = points.map { point ->
                                    mapOf(
                                        "latitude" to point.geoPoint.latitude,
                                        "longitude" to point.geoPoint.longitude,
                                        "name" to point.name.ifEmpty { point.address.ifEmpty { point.geoPoint.toString() } }
                                    )
                                }
                                routeCreationViewModel.setRoutePoints(route)
                                navController.navigate("RoutePoints")
                            },
                            buttonText = "Сохранить",
                            modifier = Modifier.weight(1f),
                            colorButton = R.color.alert_line,
                            colorText = R.color.black
                        )
                        CustomButton(
                            onClick = {

                            },
                            buttonText = "Начать",
                            modifier = Modifier.weight(1f),
                            colorButton = R.color.main,
                            colorText = R.color.white
                        )
                    }
                }
            }
        }
    }
}


