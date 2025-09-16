package com.example.travelapp2.presentation.trip.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelapp2.R
import com.example.travelapp2.data.models.LocationPointFirebase
import com.example.travelapp2.presentation.common.CustomText
import com.example.travelapp2.presentation.route.component.ImageCarousel


@Composable
fun PointInformation(selectedLocation: LocationPointFirebase?) {
    val location = selectedLocation!!
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            Text(
                text = location.title.ifEmpty { "Без названия" },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

        }
        Column(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 156.dp)
                .verticalScroll(scrollState)
        ) {

            ImageCarousel(Modifier.height(300.dp), location.photos, 300.dp - 32.dp)

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = location.city.ifEmpty { "Неизвестный город" },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Text(
                        text = location.date,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(modifier = Modifier.padding(top = 8.dp))

                Text(
                    text = location.description.ifEmpty { "Описание отсутствует" },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}