package com.example.travelapp2.presentation.place.placegeoapify.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.travelapp2.R
import com.example.travelapp2.presentation.common.CustomText
import com.example.travelapp2.presentation.place.place2gis.component.ScheduleItem
import com.example.travelapp2.presentation.route.formatOpeningHoursList

@Composable
fun ScheduleView(openingHours: String?) {
    if (openingHours.isNullOrEmpty()) {
        CustomText("Расписание отсутствует.", 14, R.font.manrope_medium, 20)
    } else {
        val openingList = formatOpeningHoursList(openingHours)
        openingList.forEach { (days, time) ->
            ScheduleItem(day = days, hours = time)
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}