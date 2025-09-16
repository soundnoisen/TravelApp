package com.example.travelapp2.presentation.recommendation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.travelapp2.R
import com.example.travelapp2.presentation.common.CustomText
import com.example.travelapp2.presentation.map.component.LocationPickerDialog
import com.example.travelapp2.presentation.recommendation.component.GenerateRouteFiltersHeader
import com.example.travelapp2.presentation.recommendation.component.RegionField
import com.example.travelapp2.presentation.viewmodels.LocationViewModel
import com.example.travelapp2.presentation.viewmodels.UserRecommendationViewModel
import org.osmdroid.util.GeoPoint

@Composable
fun GenerateRouteFiltersScreen(
    navController: NavHostController,
    userRecommendationViewModel: UserRecommendationViewModel,
    locationViewModel: LocationViewModel
) {

    val coordinates by locationViewModel.coordinates.collectAsState()
    var showLocationPicker by remember { mutableStateOf(false) }
    var geopoint by remember { mutableStateOf<GeoPoint?>(null) }

    var text by remember { mutableStateOf("") }

    LaunchedEffect(coordinates, geopoint) {
        text = if (coordinates != null) "Мое местоположение" else "Ваше местоположение не определено"
        text = if (geopoint != null) locationViewModel.getCityNameFromCoordinates(geopoint!!.latitude, geopoint!!.longitude) else text
    }


    if (showLocationPicker) {
        LocationPickerDialog(
            onLocationSelected = { selectedGeoPoint, _, _, _ ->
                geopoint = selectedGeoPoint
                userRecommendationViewModel.region = selectedGeoPoint
                showLocationPicker = false
            },
            onDismiss = { showLocationPicker = false },
            lastPoint = null,
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
            .verticalScroll(rememberScrollState())
    ) {
        Column(
        ) {
            Spacer(modifier = Modifier.padding(top = 40.dp))
            GenerateRouteFiltersHeader(onClick = { navController.popBackStack() },
                onClickApply = {
                userRecommendationViewModel.useCurrentLocation = false
                navController.popBackStack()} )

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Column(
                Modifier.padding(horizontal = 16.dp)) {

                Spacer(modifier = Modifier.height(8.dp))

                val mainColor = colorResource(id = R.color.main)

                CustomText("Область генерирования", 18, R.font.manrope_semibold, 26)
                Spacer(Modifier.height(8.dp))
                RegionField(text) { showLocationPicker = true}

                Spacer(modifier = Modifier.height(16.dp))


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomText("Количество маршрутов", 18, R.font.manrope_semibold, 20)
                    CustomText(userRecommendationViewModel.numberOfRoutes.toString(), 18, R.font.manrope_semibold, 20, color = mainColor)
                }

                Slider(
                    value = userRecommendationViewModel.numberOfRoutes.toFloat(),
                    onValueChange = { userRecommendationViewModel.numberOfRoutes = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 9,
                    colors = SliderDefaults.colors(
                        thumbColor = mainColor,
                        activeTrackColor = mainColor
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomText("Количество точек в маршруте", 18, R.font.manrope_semibold, 20)
                    CustomText(userRecommendationViewModel.placesPerRoute.toString(), 18, R.font.manrope_semibold, 20, color = mainColor)
                }

                Slider(
                    value = userRecommendationViewModel.placesPerRoute.toFloat(),
                    onValueChange = { userRecommendationViewModel.placesPerRoute = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 9,
                    colors = SliderDefaults.colors(
                        thumbColor = mainColor,
                        activeTrackColor = mainColor
                    )
                )

            }
        }
    }
}

