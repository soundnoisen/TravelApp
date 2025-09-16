package com.example.travelapp2.presentation.map.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.travelapp2.R
import com.example.travelapp2.data.models.RoutePoint
import com.example.travelapp2.presentation.util.getAddressByCoordinatesLocationIQ
import com.example.travelapp2.presentation.util.getPlaceGeoApify
import com.example.travelapp2.presentation.viewmodels.MapHolderViewModel
import kotlinx.coroutines.async
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint


@Composable
fun LocationPickerDialog(
    onLocationSelected: (GeoPoint, String?, String?, String?) -> Unit,
    onDismiss: () -> Unit,
    lastPoint: RoutePoint? = null,
) {
    val mapView = rememberMapViewWithLifecycle()
    var selectedLocation by remember { mutableStateOf<GeoPoint?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val lat = lastPoint?.geoPoint?.latitude ?: 55.796129
    val lon = lastPoint?.geoPoint?.longitude ?: 49.106414

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            color = Color.White
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        mapView.apply {
                            setTileSource(TileSourceFactory.MAPNIK)
                            setMultiTouchControls(true)
                            controller.setZoom(10.0)
                            controller.setCenter(GeoPoint(lat, lon))
                            setBuiltInZoomControls(false)
                        }
                    }
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(40.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_point_geo_location),
                        contentDescription = "Marker",
                        tint = colorResource(id = R.color.main),
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(vertical = 16.dp, horizontal = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CustomButton(
                            onClick = { onDismiss() },
                            buttonText = "Отмена",
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp),
                            colorButton = R.color.alert_line,
                            colorText = R.color.black
                        )
                        CustomButton(
                            onClick = {
                                val mapCenter = mapView.mapCenter
                                val geoPoint = GeoPoint(mapCenter.latitude, mapCenter.longitude)
                                selectedLocation = geoPoint
                                isLoading = true
                            },
                            buttonText = "Выбрать",
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp),
                            colorButton = R.color.main,
                            colorText = R.color.white
                        )
                    }
                }

                LaunchedEffect(selectedLocation) {
                    selectedLocation?.let { geoPoint ->
                        isLoading = true

                        val nameDeferred = async { getPlaceGeoApify(geoPoint.latitude, geoPoint.longitude) }
                        val addressDeferred = async { getAddressByCoordinatesLocationIQ(geoPoint.latitude, geoPoint.longitude) }

                        val name = nameDeferred.await()
                        val (address, city) = addressDeferred.await()
                        onLocationSelected(geoPoint, name, address, city)
                        isLoading = false
                        onDismiss()
                    }
                }

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
            }
        }
    }
}