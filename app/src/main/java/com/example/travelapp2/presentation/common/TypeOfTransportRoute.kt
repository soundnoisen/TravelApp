package com.example.travelapp2.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelapp2.R


@Composable
fun TypeOfTransportRoute(
    selectedTypeOfTransport: String?,
    onTypeOfTransportSelected: (String) -> Unit
) {
    val transportOptions = listOf("Пеший", "Автомобильный", "Общественный транспорт", "Велосипедный", "Комбинированный")

    Column(
        Modifier.fillMaxWidth()
    ) {
        transportOptions.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onTypeOfTransportSelected(option) }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    modifier = Modifier.size(22.dp),
                    selected = option == selectedTypeOfTransport,
                    onClick = { onTypeOfTransportSelected(option) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = colorResource(id = R.color.main),
                        unselectedColor = colorResource(id = R.color.hint)
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = option,
                    fontSize = 16.sp,
                    fontFamily = Font(R.font.inter_regular).toFontFamily()
                )
            }
        }
    }
}
