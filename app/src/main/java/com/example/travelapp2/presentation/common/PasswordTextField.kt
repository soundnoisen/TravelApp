package com.example.travelapp2.presentation.common


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.travelapp2.R


@Composable
fun PasswordTextField(
    value: String,
    placeholderText: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    val visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()

    StyledTextField(
        value = value,
        onValueChange = onValueChange,
        placeholderText = placeholderText,
        modifier = modifier.fillMaxWidth(),
        visualTransformation = visualTransformation,
        trailingIcon = {
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_eye),
                    tint = if (isPasswordVisible)
                        colorResource(id = R.color.eye)
                    else
                        colorResource(id = R.color.hint),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    )
}
