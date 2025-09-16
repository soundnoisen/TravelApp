package com.example.travelapp2.presentation.tour.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.NavigateNext
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
fun TourDate(dateStart: String, dateEnd: String, color: Color = Color.Black) {
    Row(Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_tour_date),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        CustomText(text = dateStart, 14, R.font.inter_medium, 20, color = color )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(imageVector = Icons.Default.NavigateNext, contentDescription = null, tint = color)
        Spacer(modifier = Modifier.width(4.dp))
        CustomText(text = dateEnd, 14, R.font.inter_medium, 20, color = color)
    }
}