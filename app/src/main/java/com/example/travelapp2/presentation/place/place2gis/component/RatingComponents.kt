package com.example.travelapp2.presentation.place.place2gis.component

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.travelapp2.R
import com.example.travelapp2.data.models.Place
import com.example.travelapp2.data.models.PlaceFirebase
import com.example.travelapp2.presentation.route.component.TextScreen


@Composable
fun PlaceRatingAndReviews(place: PlaceFirebase) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp).padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            RatingCard("Рейтинг", "${place.rating ?: 0.0} из 5", R.drawable.ic_star_rate_route)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Box(modifier = Modifier.weight(1f)) {
            RatingCard("Отзывы", "${place.reviewCount ?: 0} отзывов", R.drawable.ic_comments_rate_route)
        }
    }
}

@Composable
fun RatingCard(title: String, value: Any, iconRes: Int) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .height(56.dp)
            .shadow(
                16.dp,
                shape = RoundedCornerShape(10.dp),
                spotColor = Color.Black.copy(alpha = 0.06f)
            ),
        colors = CardDefaults.cardColors(Color.White),
        onClick = {}
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(start = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .shadow(
                        6.dp,
                        shape = RoundedCornerShape(60.dp),
                        spotColor = Color.Black.copy(alpha = 0.2f)
                    )
                    .background(Color.White, shape = CircleShape)
            ) {
                IconButton(onClick = {}, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(id = iconRes),
                        tint = colorResource(id = R.color.main),
                        contentDescription = null
                    )
                }
            }
            Column(Modifier.padding(start = 10.dp)) {
                TextScreen(title, colorResource(id = R.color.hint))
                TextScreen("$value", Color.Black)
            }
        }
    }
}