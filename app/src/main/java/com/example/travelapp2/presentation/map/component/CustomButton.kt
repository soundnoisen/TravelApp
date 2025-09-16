package com.example.travelapp2.presentation.map.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelapp2.R

@Composable
fun CustomButton(
    onClick: () -> Unit,
    buttonText: String,
    modifier: Modifier = Modifier,
    colorButton: Int,
    colorText: Int,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(colorResource(colorButton)),
        modifier = modifier
            .height(44.dp),
        shape = RoundedCornerShape(10.dp)
    )
    {
        Text(
            text = buttonText,
            color = colorResource(id = colorText),
            fontSize = 16.sp,
            fontFamily = Font(R.font.inter_medium).toFontFamily()
        )
    }
}