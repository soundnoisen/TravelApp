package com.example.travelapp2.presentation.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelapp2.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomField(
    text: String,
    placeholder: String,
    onTextChanged: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                16.dp,
                shape = RoundedCornerShape(10.dp),
                spotColor = Color.Black.copy(alpha = 0.06f)
            )
            .background(Color.White, shape = RoundedCornerShape(10.dp)),
    ) {
        TextField(
            value = text,
            onValueChange = { input ->
                onTextChanged(input)
            },
            placeholder = {
                Text(
                    text = placeholder,
                    color = colorResource(id = R.color.hint),
                    fontSize = 16.sp,
                    fontFamily = Font(R.font.inter_regular).toFontFamily()
                )
            },
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.Black,
                fontFamily = Font(R.font.inter_regular).toFontFamily()
            ),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
        )
    }
}