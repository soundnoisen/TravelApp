package com.example.travelapp2.presentation.profile.profileInformation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelapp2.R


@Composable
fun CustomOutlinedField(
    text: String,
    placeholder: String,
    onTextChanged: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(60))
            .border(1.dp, Color.Gray, RoundedCornerShape(60))
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = text,
            onValueChange = onTextChanged,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontFamily = Font(R.font.inter_regular).toFontFamily(),
                color = Color.Black
            ),
            decorationBox = { innerTextField ->
                if (text.isEmpty()) {
                    Text(
                        text = placeholder,
                        fontSize = 14.sp,
                        fontFamily = Font(R.font.inter_regular).toFontFamily(),
                        color = colorResource(id = R.color.hint)
                    )
                }
                innerTextField()
            }
        )
    }
}
