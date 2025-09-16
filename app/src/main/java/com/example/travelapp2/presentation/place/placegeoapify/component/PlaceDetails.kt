package com.example.travelapp2.presentation.place.placegeoapify.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.travelapp2.R
import com.example.travelapp2.presentation.common.CustomText
import com.example.travelapp2.data.models.GeoApifyPlace
import com.example.travelapp2.data.models.PlaceFirebase


@Composable
fun PlaceDetails(place: PlaceFirebase) {
    if (place.description.isEmpty() && place.phone.isEmpty() && place.website.isEmpty()){
        CustomText("Описание отсутствует.", 14, R.font.manrope_medium, 20)
        return
    }
    if (place.description.isNotEmpty()) {
        CustomText("Описание: ${place.description}", 14, R.font.manrope_medium, 20)
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
    }
    if (place.phone.isNotEmpty()) {
        CustomText("Телефон: ${place.phone}", 14, R.font.manrope_medium, 20)
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
    }
    if (place.website.isNotEmpty()) {
        CustomText("Сайт: ${place.website}", 14, R.font.manrope_medium, 20)
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
    }
}
