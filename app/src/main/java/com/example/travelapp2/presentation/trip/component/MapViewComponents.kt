package com.example.travelapp2.presentation.trip.component

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.travelapp2.R
import com.example.travelapp2.data.models.LocationPointFirebase
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

@Composable
fun MapViewComposable(points: List<LocationPointFirebase>?, selectedIndex: Int) {
    if (points.isNullOrEmpty()) {
        return
    }

    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            controller.setZoom(14)
            setMultiTouchControls(true)
            setBuiltInZoomControls(false)
        }
    }

    val isFirstLoad = remember { mutableStateOf(true) }

    val selectedIndexState by rememberUpdatedState(selectedIndex)

    LaunchedEffect(points, selectedIndexState) {
        mapView.overlays.clear()

        val mapController = mapView.controller
        val locations = points.map {
            GeoPoint(it.latitude, it.longitude)
        }

        if (locations.isNotEmpty()) {
            if (isFirstLoad.value) {
                mapController.setCenter(locations[0])
                isFirstLoad.value = false
            } else {
                mapController.animateTo(locations[selectedIndexState])
            }
        }

        val line = Polyline().apply {
            setPoints(ArrayList(locations))
            color = android.graphics.Color.WHITE
            width = 8f
        }
        mapView.overlays.add(line)

        for ((index, location) in points.withIndex()) {
            createCustomMarkerDrawable(context, location.photos.firstOrNull(), index == selectedIndexState) { drawable ->
                val marker = Marker(mapView).apply {
                    position = GeoPoint(location.latitude, location.longitude)
                    title = location.name
                    icon = drawable
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                }
                mapView.overlays.add(marker)
                mapView.invalidate()
            }
        }
        mapView.invalidate()
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { mapView }
    )
}


fun createCustomMarkerDrawable(context: Context, photoUrl: String?, isSelected: Boolean, callback: (Drawable) -> Unit) {
    val size = 100
    val padding = 20
    val bitmapWithCircle = Bitmap.createBitmap(size + padding * 2, size + padding * 2, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmapWithCircle)
    val colorWithOutImage = ContextCompat.getColor(context, R.color.hint)
    val selectedColor = ContextCompat.getColor(context, R.color.main)

    val paint = Paint().apply { isAntiAlias = true }
    val radius = size / 2f
    val center = radius + padding

    if (isSelected) {
        paint.maskFilter = BlurMaskFilter(10f, BlurMaskFilter.Blur.NORMAL)
        paint.color = selectedColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f
        canvas.drawCircle(center, center, radius - paint.strokeWidth / 2, paint)
        paint.maskFilter = null
    }

    paint.color = if (isSelected) selectedColor else android.graphics.Color.WHITE
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 8f
    canvas.drawCircle(center, center, radius - paint.strokeWidth / 2, paint)

    paint.style = Paint.Style.FILL
    paint.color = android.graphics.Color.WHITE
    canvas.drawCircle(center, center, radius - 8f, paint)

    if (photoUrl.isNullOrEmpty()) {
        paint.color = colorWithOutImage
        canvas.drawCircle(center, center, radius - 8f, paint)
        callback(BitmapDrawable(context.resources, bitmapWithCircle))
        return
    }

    Glide.with(context)
        .asBitmap()
        .load(photoUrl)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                val scaledBitmap = Bitmap.createScaledBitmap(resource, size, size, true)
                val shader = BitmapShader(scaledBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
                paint.shader = shader
                canvas.drawCircle(center, center, radius - 8f, paint)

                callback(BitmapDrawable(context.resources, bitmapWithCircle))
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                callback(BitmapDrawable(context.resources, bitmapWithCircle))
            }
        })
}
