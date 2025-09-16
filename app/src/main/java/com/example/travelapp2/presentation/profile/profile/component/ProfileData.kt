package com.example.travelapp2.presentation.profile.profile.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.sp
import com.example.travelapp2.R

@Composable
fun ProfileData(number: Int, category: String, onClick: () -> Unit) {
    Column(Modifier.clickable(
        onClick = onClick,
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    )) {
        Text(
            text = number.toString(),
            fontSize = 14.sp,
            fontFamily = Font(R.font.manrope_semibold).toFontFamily(),
            style = LocalTextStyle.current.copy(lineHeight = 16.sp)
        )
        Text(
            text = category,
            fontSize = 14.sp,
            fontFamily = Font(R.font.inter_regular).toFontFamily(),
            style = LocalTextStyle.current.copy(lineHeight = 16.sp),
            //color = colorResource(id = R.color.hint)
        )
    }
}