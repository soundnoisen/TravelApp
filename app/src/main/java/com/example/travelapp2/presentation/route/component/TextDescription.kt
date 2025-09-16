package com.example.travelapp2.presentation.route.component

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelapp2.R
import com.example.travelapp2.data.models.RouteFirebase
import com.example.travelapp2.presentation.common.CustomText

@Composable
fun TextDescription(route: RouteFirebase) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        item {
            InfoCard {
                InfoBlock(title = "Описание", content = route.description.ifEmpty { "Описание отсутствует" })
                InfoBlock("Время в пути:", route.routeTime)
                InfoBlock("Тип маршрута:", route.typesOfRoute.joinToString("; "))
                InfoBlock("Сложность маршрута:", route.difficultyLevel)
                InfoBlock("Тип передвижения:", route.typeOfTransport)
                InfoBlock(
                    "Ценовой диапазон ₽:",
                    "${route.budgetRange["start"] ?: 0}₽ — ${route.budgetRange["end"] ?: 0}₽"
                )

                if (route.convenience.isNotEmpty()) {
                    InfoBlock("Удобства:", route.convenience.joinToString("\n") { "- $it" })
                }

                if (route.recommendations.isNotEmpty()) {
                    InfoBlock("Рекомендации:", route.recommendations, showDivider = false)
                }
            }
            Spacer(modifier = Modifier.height(102.dp))
        }
    }
}


@Composable
fun InfoBlock(title: String, content: String, showDivider: Boolean = true) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = content,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
        if (showDivider) {
            Divider(modifier = Modifier.padding(vertical = 12.dp))
        }
    }
}

@Composable
fun InfoCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.background)
        )
    ) {
        Column(modifier = Modifier.padding(vertical = 16.dp), content = content)
    }
}

