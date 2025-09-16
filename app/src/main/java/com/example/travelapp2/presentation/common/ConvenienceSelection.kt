package com.example.travelapp2.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelapp2.R

@Composable
fun ConvenienceSelection(
    selectedConveniences: Set<String>,
    onConvenienceSelected: (String, Boolean) -> Unit
) {
    val conveniencesList = listOf(
        "Аренда автомобиля",
        "Бесплатный Wi-Fi",
        "Экскурсии с гидом",
        "Местные рестораны и кухня",
        "Варианты для семейного отдыха",
        "Зоны отдыха",
        "Услуги для людей с ограниченными возможностями",
        "Магазины сувениров"
    )

    Column() {
        conveniencesList.forEach { convenience ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val isChecked = !selectedConveniences.contains(convenience)
                        onConvenienceSelected(convenience, isChecked)
                    }
                    .padding(vertical = 8.dp)
            ) {
                CircularCheckbox(
                    checked = selectedConveniences.contains(convenience),
                    onCheckedChange = { isChecked ->
                        onConvenienceSelected(convenience, isChecked)
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = convenience,
                    fontSize = 16.sp,
                    fontFamily = Font(R.font.inter_regular).toFontFamily()
                )
            }
        }
    }
}