package com.example.travelapp2.presentation.tours.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.travelapp2.data.models.TourFirebase
import com.example.travelapp2.emptyContentPhoto

@Composable
fun ImageTour(tour: TourFirebase) {
    val imageUrl = tour.photoUrl.ifEmpty {
        emptyContentPhoto
    }

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .alpha(1f),
        contentScale = ContentScale.Crop
    )
}