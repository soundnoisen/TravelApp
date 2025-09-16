package com.example.travelapp2.main.home.filters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.travelapp2.R
import com.example.travelapp2.presentation.common.CustomText

import com.example.travelapp2.main.home.filters.component.HeaderFilters
import com.example.travelapp2.presentation.common.CitySelectionField
import com.example.travelapp2.presentation.common.DifficultyLevelSelector
import com.example.travelapp2.presentation.common.DurationSelection
import com.example.travelapp2.presentation.common.NumberOfDays
import com.example.travelapp2.presentation.common.PriceRoute
import com.example.travelapp2.presentation.common.TypeOfRouteSelection
import com.example.travelapp2.presentation.common.TypeOfTransportRoute
import com.example.travelapp2.presentation.viewmodels.RouteFirebaseViewModel

@Composable
fun FiltersScreen(navController: NavHostController,   filtersViewModel: FiltersViewModel
) {

    val filter by filtersViewModel.filter.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
            .verticalScroll(rememberScrollState())
    ) {
        Column(
        ) {
            Spacer(modifier = Modifier.padding(top = 40.dp))
            HeaderFilters(navController) {
                navController.popBackStack()
            }
            Spacer(modifier = Modifier.padding(top = 8.dp))

            Column(
                Modifier.padding(horizontal = 16.dp)) {

                Spacer(modifier = Modifier.height(8.dp))

                CustomText("Местоположение", 18, R.font.manrope_semibold, 26)
                Spacer(Modifier.height(8.dp))
                CitySelectionField(
                    selectedCity = filter.city,
                    onCitySelected = filtersViewModel::setCity
                )

                Spacer(Modifier.height(16.dp))
                CustomText("Длительность маршрута", 18, R.font.manrope_semibold, 26)
                Spacer(Modifier.height(8.dp))
                DurationSelection(
                    selectedRouteTime = filter.routeTime,
                    onRouteTimeSelected = filtersViewModel::setRouteTime
                )


                if (filter.routeTime == "День или больше") {
                    Spacer(Modifier.height(16.dp))
                    CustomText("Примерное количество дней", 18, R.font.manrope_semibold, 26)
                    Spacer(Modifier.height(8.dp))
                    NumberOfDays(
                        days = filter.exactDays,
                        onDaysChanged = filtersViewModel::setExactDays
                    )
                }

                Spacer(Modifier.height(16.dp))
                CustomText("Ценовой диапазон ₽", 18, R.font.manrope_semibold, 26)
                Spacer(Modifier.height(8.dp))
                PriceRoute(
                    priceStart = filter.budgetStart,
                    priceEnd = filter.budgetEnd,
                    onPriceStartChanged = { filtersViewModel.setBudget(it, filter.budgetEnd) },
                    onPriceEndChanged = { filtersViewModel.setBudget(filter.budgetStart, it) },
                    errorMessage = null
                )

                Spacer(Modifier.height(16.dp))
                CustomText("Сложность маршрута", 18, R.font.manrope_semibold, 26)
                Spacer(Modifier.height(8.dp))
                DifficultyLevelSelector(
                    selectedDifficultyLevel = filter.difficulty,
                    onDifficultyLevelSelected = filtersViewModel::setDifficulty
                )

                Spacer(Modifier.height(16.dp))
                CustomText("Тип маршрута", 18, R.font.manrope_semibold, 26)
                TypeOfRouteSelection(
                    selectedTypes = filter.routeTypes,
                    onTypeSelected = { type, checked ->
                        if (checked) filtersViewModel.addRouteType(type)
                        else        filtersViewModel.removeRouteType(type)
                    }
                )

                Spacer(Modifier.height(16.dp))
                CustomText("Тип передвижения", 18, R.font.manrope_semibold, 26)
                TypeOfTransportRoute(
                    selectedTypeOfTransport = filter.transportType,
                    onTypeOfTransportSelected = filtersViewModel::setTransportType
                )
            }
        }
    }
}

