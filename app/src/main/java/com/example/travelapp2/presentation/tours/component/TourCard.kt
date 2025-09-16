package com.example.travelapp2.presentation.tours.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelapp2.R
import com.example.travelapp2.data.models.TourFirebase
import com.example.travelapp2.presentation.tour.component.TourDate

@Composable
fun TourCard(tour: TourFirebase, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .shadow(
                16.dp,
                shape = RoundedCornerShape(10.dp),
                spotColor = Color.Black.copy(alpha = 0.06f)
            )
        ,
        colors = CardDefaults.cardColors(Color.White),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            contentAlignment = Alignment.Center
        ) {
            ImageTour(tour)
        }

        Box(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
            Column {
                Text(
                    maxLines = 1,
                    text = tour.title,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = Font(R.font.manrope_semibold).toFontFamily(),
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                TourDate(tour.startDate, tour.endDate)
                Spacer(modifier = Modifier.height(4.dp))
                TourParticipantsNumber(tour.participants.size, tour.maxParticipants)
            }
        }
    }
}
