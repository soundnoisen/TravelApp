package com.example.travelapp2.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelapp2.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceRoute(
    priceStart: String,
    priceEnd: String,
    onPriceStartChanged: (String) -> Unit,
    onPriceEndChanged: (String) -> Unit,
    errorMessage: String?
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PriceInputField(
                value = priceStart,
                onValueChange = onPriceStartChanged,
                modifier = Modifier.width(160.dp)
            )
            Text(
                text = "—",
                modifier = Modifier.padding(horizontal = 8.dp),
                style = TextStyle(fontSize = 16.sp, color = Color.Black)
            )
            PriceInputField(
                value = priceEnd,
                onValueChange = onPriceEndChanged,
                modifier = Modifier.width(160.dp)
            )
        }
        errorMessage?.let { OnValidationError(it) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = "0"
) {
    Box(
        modifier = modifier
            .height(56.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(10.dp),
                spotColor = Color.Black.copy(alpha = 0.06f)
            )
            .background(color = Color.White, shape = RoundedCornerShape(10.dp))
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholderText,
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
            modifier = Modifier.fillMaxSize(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
    }
}

