package com.example.travelapp2.presentation.place.place2gis.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.travelapp2.R
import com.example.travelapp2.presentation.common.CustomText
import com.example.travelapp2.data.models.Place
import com.example.travelapp2.data.models.PlaceFirebase


@Composable
fun PlaceComments (place: PlaceFirebase) {
    Column {
        place.comment.takeIf { it.isNotEmpty() }?.let { comment ->
            CustomText("Комментарий к расписанию: $comment", 14, R.font.manrope_medium, 20)
            Spacer(modifier = Modifier.padding(bottom = 8.dp))
        }
        place.addressComment.takeIf { it.isNotEmpty() }?.let { addressComment ->
            CustomText("Комментарий к адресу: $addressComment", 14, R.font.manrope_medium, 20)
            Spacer(modifier = Modifier.padding(bottom = 8.dp))
        }
        if (place.addressComment.isEmpty() && place.comment.isEmpty()) {
            CustomText("Комментарии отсутсвуют.", 14, R.font.manrope_medium, 20)
        }
    }
}