package com.example.travelapp2.presentation.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.osmdroid.api.IGeoPoint
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import javax.inject.Inject


@HiltViewModel
class MapHolderViewModel @Inject constructor() : ViewModel() {
    var lastCenter: GeoPoint? by mutableStateOf(null)
    var lastZoom: Double? by mutableStateOf(null)

    fun saveMapState(center: GeoPoint, zoom: Double) {
        lastCenter = center
        lastZoom = zoom
    }
}

