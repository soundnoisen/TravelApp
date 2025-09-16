package com.example.travelapp2.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.sp
import com.example.travelapp2.R



@Composable
fun CustomText(text: String, size: Int, fontFamilyId: Int, lineHeight: Int,
               modifier: Modifier = Modifier, color: Color = Color.Black) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = size.sp,
        color = color,
        fontFamily = Font(fontFamilyId).toFontFamily(),
        style = LocalTextStyle.current.copy(lineHeight = lineHeight.sp)
    )
}


@Composable
fun TextViewAll(onClick: () -> Unit) {
    Text(
        text = "Все",
        fontSize = 16.sp,
        color = colorResource(id = R.color.main),
        fontFamily = Font(R.font.manrope_semibold).toFontFamily(),
        style = LocalTextStyle.current.copy(lineHeight = 26.sp),
        modifier = Modifier.clickable(onClick = onClick,
            interactionSource = remember { MutableInteractionSource() },
            indication = null)
    )
}




