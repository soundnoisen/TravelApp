package com.example.travelapp2.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelapp2.R

@Composable
fun OnValidationError(error: String) {
    Text(
        text = error,
        color = Color.Red,
        fontFamily = Font(R.font.inter_regular).toFontFamily(),
        fontSize = 14.sp,
        modifier = Modifier.padding(top = 8.dp)
    )
}