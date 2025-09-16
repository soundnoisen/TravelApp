package com.example.travelapp2.presentation.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelapp2.R
import com.example.travelapp2.presentation.common.CustomText

@Composable
fun HomeHeader(locationName: String, onLocationClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier
        .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(60.dp))
                .clickable { onLocationClick() }
                .padding(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.ic_point_geo_location),
                    tint = colorResource(id = R.color.main),
                    contentDescription = null
                )
                Text(
                    text = locationName,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = Font(R.font.manrope_semibold).toFontFamily(),
                    fontSize = 18.sp,
                    style = LocalTextStyle.current.copy(lineHeight = 20.sp),
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}
