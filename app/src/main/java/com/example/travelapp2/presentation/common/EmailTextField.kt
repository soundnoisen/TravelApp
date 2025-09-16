package com.example.travelapp2.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun EmailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    StyledTextField(
        value = value,
        onValueChange = onValueChange,
        placeholderText = "Введите email",
        modifier = modifier.fillMaxWidth()
    )
}
