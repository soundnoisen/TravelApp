package com.example.travelapp2.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelapp2.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPlaceField(
    text: String,
    onTextChange: (String) -> Unit,
    onClickSearchPlace: () -> Unit,
    modifier: Modifier = Modifier,
    modifier2: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                16.dp,
                shape = RoundedCornerShape(10.dp),
                spotColor = Color.Black.copy(alpha = 0.06f)
            )
            .background(Color.White, shape = RoundedCornerShape(10.dp))
    ) {
        TextField(
            value = text,
            onValueChange = onTextChange,
            modifier = modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            placeholder = {
                Text(
                    text = "Искать место",
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
            trailingIcon = {
                IconButton(onClick = onClickSearchPlace, modifier = modifier2) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Поиск",
                        tint = colorResource(id = R.color.hint),
                        modifier = Modifier.size(22.dp)
                    )
                }
            },
            singleLine = true
        )
    }
}