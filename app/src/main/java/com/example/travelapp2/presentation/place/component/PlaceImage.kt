package com.example.travelapp2.presentation.place.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.travelapp2.R

@Composable
fun LoadPlaceImage(protoUrl: String?) {
    if (!protoUrl.isNullOrEmpty()) {
        AsyncImage(
            model = protoUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(550.dp),
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.im_no_photo_route),
            contentDescription = "Default Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(550.dp),
            contentScale = ContentScale.Crop
        )
    }
}