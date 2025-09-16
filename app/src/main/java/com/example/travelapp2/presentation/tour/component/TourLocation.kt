package com.example.travelapp2.presentation.tour.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShareLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.travelapp2.R
import com.example.travelapp2.presentation.common.CustomText

@Composable
fun TourLocation2(city: String) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(contentDescription = null, painter = painterResource(id = R.drawable.ic_tour_location),  modifier = Modifier.size(20.dp), tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        CustomText(text = city, 16, R.font.inter_medium, 20, color = Color.White)
    }
}

