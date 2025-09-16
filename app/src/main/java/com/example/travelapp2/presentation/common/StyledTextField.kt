package com.example.travelapp2.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelapp2.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    var isHintDisplayed by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .height(56.dp)
            .shadow(
                16.dp,
                shape = RoundedCornerShape(10.dp),
                spotColor = Color.Black.copy(alpha = 0.06f)
            )
            .background(Color.White, shape = RoundedCornerShape(10.dp))
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            placeholder = {
                if (!isHintDisplayed) {
                    Text(
                        text = placeholderText,
                        color = colorResource(id = R.color.hint),
                        fontSize = 16.sp,
                        fontFamily = Font(R.font.inter_regular).toFontFamily()
                    )
                }
            },
            modifier = modifier
                .fillMaxSize()
                .onFocusChanged { focusState ->
                    isHintDisplayed = focusState.isFocused
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
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )
    }
}
