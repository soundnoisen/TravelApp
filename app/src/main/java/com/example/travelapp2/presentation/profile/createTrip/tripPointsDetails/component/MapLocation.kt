package com.example.travelapp2.presentation.profile.createTrip.tripPointsDetails.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.travelapp2.R
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapLocation(lat: Double, lon: Double) {
    val context = LocalContext.current

    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            isClickable = false
            setMultiTouchControls(false)
            setBuiltInZoomControls(false)
            setTilesScaledToDpi(true)
        }
    }

    val markerPoint = remember { GeoPoint(lat, lon) }

    val markerOverlay = remember {
        object : Marker(mapView) {
            init {
                position = markerPoint
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                icon = ContextCompat.getDrawable(context, R.drawable.ic_point_geo_location)
            }
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxWidth().height(300.dp),
        factory = { mapView },
        update = {
            it.controller.setZoom(10.0)
            it.controller.setCenter(markerPoint)

            it.overlays.add(markerOverlay)
        }
    )
}

