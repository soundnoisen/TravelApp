package com.example.travelapp2.presentation.place.place2gis.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.travelapp2.R
import com.example.travelapp2.data.models.Place
import com.example.travelapp2.data.models.PlaceFirebase
import com.example.travelapp2.presentation.common.CustomText
import com.example.travelapp2.presentation.route.component.TextTab


@Composable
fun ScheduleView(place: PlaceFirebase?) {
    if (place?.schedule == null) {
        CustomText("Расписание отсутствует.", 14, R.font.manrope_medium, 20)
        return
    }
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    place.schedule.let { schedule ->
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (place.is24x7) {
                CustomText("Работает круглосуточно.", 14, R.font.manrope_medium, 20)
            } else if (schedule.isNotEmpty()) {
                daysOfWeek.forEach { day ->
                    schedule[day]?.let { hoursList ->
                        val translatedDay = translateDay(day)
                        val hoursText = hoursList.joinToString { timeMap ->
                            "${timeMap["from"]} - ${timeMap["to"]}"
                        }
                        ScheduleItem(
                            day = translatedDay,
                            hours = hoursText
                        )
                    }
                }
            } else {
                TextTab("Расписание отсутствует.", 14,  FontWeight.Normal, 20, Color.Black)
            }
        }
    }

}

@Composable
fun ScheduleItem(day: String, hours: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CustomText(day, 14, R.font.manrope_medium, 20)
        CustomText(hours, 14, R.font.manrope_medium, 20)
    }
}

fun translateDay(day: String): String {
    return when (day) {
        "Mon" -> "Понедельник"
        "Tue" -> "Вторник"
        "Wed" -> "Среда"
        "Thu" -> "Четверг"
        "Fri" -> "Пятница"
        "Sat" -> "Суббота"
        "Sun" -> "Воскресенье"
        else -> day
    }
}