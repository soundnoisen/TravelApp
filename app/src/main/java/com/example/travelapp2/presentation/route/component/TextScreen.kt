package com.example.travelapp2.presentation.route.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.travelapp2.R


@Composable
fun TextScreen(text: String, color: Color){
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontFamily = Font(R.font.inter_regular).toFontFamily(),
        fontSize = 12.sp,
        color = color,
        style = LocalTextStyle.current.copy(lineHeight = 14.sp),
    )
}