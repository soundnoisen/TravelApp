package com.example.travelapp2.presentation.route.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextTab(text: String, size: Int, font: FontWeight, lineHeight: Int, color: Color, dp: Int = 16){
    Text(
        text = text,
        fontSize = size.sp,
        fontWeight = font,
        color = color,
        style = LocalTextStyle.current.copy(lineHeight = lineHeight.sp),
        modifier = Modifier.padding(top = dp.dp).padding(bottom = 16.dp)
    )
}

@Composable
fun TextView(text: String, size: Int, font: FontWeight, color: Color){
    Text(
        text = text,
        fontSize = size.sp,
        fontWeight = font,
        color = color,
    )
}

