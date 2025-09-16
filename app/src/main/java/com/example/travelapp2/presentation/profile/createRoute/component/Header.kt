package com.example.travelapp2.presentation.profile.createRoute.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelapp2.R

@Composable
fun Header(
    title: String,
    startIcon: Painter? = null,
    startIconAction: (() -> Unit)? = null,
    endIcon: Painter? = null,
    endIconAction: (() -> Unit)? = null,
    tint: Color = Color.Black
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            overflow = TextOverflow.Ellipsis,
            fontFamily = Font(R.font.manrope_semibold).toFontFamily(),
            fontSize = 18.sp,
            color = tint,
            style = LocalTextStyle.current.copy(lineHeight = 20.sp),
            modifier = Modifier.align(Alignment.Center)
        )

        if (startIcon != null && startIconAction != null) {
            IconButton(
                onClick = startIconAction,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = startIcon,
                    tint = tint,
                    contentDescription = null
                )
            }
        }

        if (endIcon != null && endIconAction != null) {
            IconButton(
                onClick = endIconAction,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    painter = endIcon,
                    tint = tint,
                    contentDescription = null
                )
            }
        }
    }
}


