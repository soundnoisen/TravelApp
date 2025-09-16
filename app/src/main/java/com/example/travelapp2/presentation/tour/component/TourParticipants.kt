package com.example.travelapp2.presentation.tour.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun TourParticipants(
    participants: List<Pair<String, String>>,
    navController: NavHostController,
    currentUserId: String
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        participants.forEachIndexed { index, avatarRes ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(avatarRes.second.ifEmpty { "https://res.cloudinary.com/daszxuaam/image/upload/v1745168269/img_no_photo_user_ir8f4n.png" })
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .zIndex(index.toFloat())
                    .then(
                        if (index > 0) Modifier.offset(x = (-12 * index).dp)
                        else Modifier
                    )
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape)
                    .background(Color.LightGray)
                    .clickable {
                        if (currentUserId != avatarRes.first) {
                            navController.navigate("Profile/${avatarRes.first}")
                        }
                    },
                contentScale = ContentScale.Crop
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .zIndex(participants.size.toFloat())
                .offset(x = (-12 * participants.size).dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFe0e0e0))
                .clickable {}
                .border(2.dp, Color.White, CircleShape)
        ) {
            Text("+", fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}
