package com.example.travelapp2.presentation.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelapp2.R


@Composable
fun DurationSelection(
    selectedRouteTime: String?,
    onRouteTimeSelected: (String) -> Unit
) {
    val options = listOf("Пару часов", "4–6 часов", "День или больше")

    Column {
        DurationFilterChips(
            options = options,
            selectedRouteTime = selectedRouteTime,
            onRouteTimeSelected = onRouteTimeSelected
        )
    }
}


@Composable
fun DurationFilterChips(
    options: List<String>,
    selectedRouteTime: String?,
    onRouteTimeSelected: (String) -> Unit
) {
    val selectedOption = remember { mutableStateOf<String?>(null) }

    LazyRow {
        items(options) { label ->
            val isSelected = label == selectedRouteTime

            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) colorResource(id = R.color.main) else Color.White,
                animationSpec = tween(durationMillis = 300), label = ""
            )

            val textColor by animateColorAsState(
                targetValue = if (isSelected) Color.White else Color.Black,
                animationSpec = tween(durationMillis = 300), label = ""
            )

            Surface(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .height(56.dp)
                    .shadow(
                        16.dp,
                        RoundedCornerShape(10.dp),
                        spotColor = Color.Black.copy(alpha = 0.06f)
                    )
                    .background(backgroundColor, shape = RoundedCornerShape(10.dp))
                    .clickable {
                        selectedOption.value = label
                        onRouteTimeSelected(label)
                    },
                shape = RoundedCornerShape(10.dp),
                color = backgroundColor
            ) {
                Text(
                    text = label,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontFamily = Font(R.font.manrope_semibold).toFontFamily(),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}