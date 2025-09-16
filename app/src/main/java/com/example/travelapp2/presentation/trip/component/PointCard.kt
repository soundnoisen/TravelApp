package com.example.travelapp2.presentation.trip.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.travelapp2.R
import com.example.travelapp2.emptyContentPhoto


@Composable
fun PointCard(isActive: Boolean, city: String?, name: String?, image: String?, onClick: () -> Unit) {
    val imageUrl = if (image.isNullOrEmpty()) {
        emptyContentPhoto
    } else {
        image
    }

    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .width(184.dp)
            .shadow(
                16.dp,
                shape = RoundedCornerShape(10.dp),
                spotColor = Color.Black.copy(alpha = 0.06f)
            )
            .clickable { onClick() },
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Box {
            Column {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .height(100.dp)
                        .width(184.dp),
                    contentScale = ContentScale.Crop
                )

                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Column {
                        Text(
                            text = if (name.isNullOrEmpty()) "Без названия" else name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )

                        Text(
                            text = if (city.isNullOrEmpty()) "Неизвестый город" else city,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = colorResource(id = R.color.hint),
                        )

                    }
                }
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(if (isActive) colorResource(id = R.color.main) else Color.Transparent)
            )
        }
    }
}
