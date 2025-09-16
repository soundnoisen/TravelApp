package com.example.travelapp2.presentation.route.component

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.travelapp2.R
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker


@Composable
fun PrintMap(route: List<Map<String, Any>>) {
    val context = LocalContext.current

    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            controller.setZoom(14)
            setMultiTouchControls(true)
            setBuiltInZoomControls(false)
        }
    }

    val isInitialized = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!isInitialized.value && route.isNotEmpty()) {
            mapView.overlays.clear()

            val mapController = mapView.controller

            val firstPoint = route[0]
            val latitude = firstPoint["latitude"] as? Double ?: 0.0
            val longitude = firstPoint["longitude"] as? Double ?: 0.0
            val geoPoint = GeoPoint(latitude, longitude)
            mapController.setCenter(geoPoint)

            for ((index, point) in route.withIndex()) {
                val lat = point["latitude"] as? Double ?: 0.0
                val lon = point["longitude"] as? Double ?: 0.0
                val name = point["name"] as? String ?: "Unnamed Point"
                val geoPointItem = GeoPoint(lat, lon)

                val marker = Marker(mapView).apply {
                    position = geoPointItem
                    icon = createCustomMarkerDrawable(context, index + 1)
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                    title = name
                }
                mapView.overlays.add(marker)
            }

            mapView.invalidate()
            isInitialized.value = true
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { mapView }
    )
}


@Composable
fun ShowMap(
    mapView: MapView,
    route: List<Map<String, Any>>,
    isInitialized: MutableState<Boolean>
) {
    val context = LocalContext.current

    LaunchedEffect(route) {
        if (!isInitialized.value && route.isNotEmpty()) {
            mapView.overlays.clear()

            val controller = mapView.controller
            val firstPoint = route.first()
            val lat = firstPoint["latitude"] as? Double ?: 0.0
            val lon = firstPoint["longitude"] as? Double ?: 0.0
            controller.setCenter(GeoPoint(lat, lon))

            route.forEachIndexed { index, point ->
                val latitude = point["latitude"] as? Double ?: 0.0
                val longitude = point["longitude"] as? Double ?: 0.0
                val name = point["name"] as? String ?: "Unnamed Point"
                val geoPoint = GeoPoint(latitude, longitude)

                val marker = Marker(mapView).apply {
                    position = geoPoint
                    icon = createCustomMarkerDrawable(context, index + 1)
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                    title = name
                }

                mapView.overlays.add(marker)
            }

            mapView.invalidate()
            isInitialized.value = true
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { mapView }
    )
}



fun createCustomMarkerDrawable(context: Context, index: Int): Drawable {
    val size = 70
    val padding = 20
    val bitmapWithCircle = Bitmap.createBitmap(size + padding * 2, size + padding * 2, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmapWithCircle)
    canvas.drawColor(android.graphics.Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

    val selectedColor = ContextCompat.getColor(context, R.color.main)

    val paint = Paint().apply {
        isAntiAlias = true
    }

    val radius = size / 2f
    val center = radius + padding

    paint.style = Paint.Style.FILL
    paint.color = android.graphics.Color.WHITE
    canvas.drawCircle(center, center, radius, paint)

    paint.style = Paint.Style.STROKE
    paint.color = selectedColor
    paint.strokeWidth = 6f
    canvas.drawCircle(center, center, radius - paint.strokeWidth / 2, paint)

    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    paint.style = Paint.Style.FILL
    paint.color = selectedColor
    paint.textSize = 30f
    paint.textAlign = Paint.Align.CENTER

    val text = index.toString()
    val textBounds = Rect()
    paint.getTextBounds(text, 0, text.length, textBounds)

    canvas.drawText(text, center, center + textBounds.height() / 2f, paint)

    return BitmapDrawable(context.resources, bitmapWithCircle)
}