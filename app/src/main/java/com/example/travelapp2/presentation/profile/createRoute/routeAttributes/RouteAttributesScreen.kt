package com.example.travelapp2.presentation.profile.createRoute.routeAttributes

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.travelapp2.R
import com.example.travelapp2.data.models.RouteFirebase
import com.example.travelapp2.domain.validation.validatePriceRange
import com.example.travelapp2.presentation.common.TypeOfTransportRoute
import com.example.travelapp2.presentation.common.CitySelectionField
import com.example.travelapp2.presentation.common.ConvenienceSelection
import com.example.travelapp2.presentation.common.CustomText
import com.example.travelapp2.presentation.common.DifficultyLevelSelector
import com.example.travelapp2.presentation.common.DurationSelection
import com.example.travelapp2.presentation.common.NumberOfDays
import com.example.travelapp2.presentation.common.PriceRoute
import com.example.travelapp2.presentation.common.TypeOfRouteSelection
import com.example.travelapp2.presentation.profile.createRoute.component.Header
import com.example.travelapp2.presentation.profile.createRoute.component.showToast
import com.example.travelapp2.presentation.profile.createRoute.RouteCreationViewModel


@Composable
fun RouteAttributesScreen(navController: NavHostController, routeCreationViewModel: RouteCreationViewModel) {
    val context = LocalContext.current

    val routeData by routeCreationViewModel.routeData.observeAsState(RouteFirebase())

    var selectedCity by remember { mutableStateOf(routeData.city) }
    var selectedRouteTime by remember { mutableStateOf(routeData.routeTime) }
    var exactDays by remember { mutableStateOf(routeData.exactDays.toString()) }
    var selectedDifficultyLevel by remember { mutableStateOf(routeData.difficultyLevel) }
    var budgetStart by remember { mutableStateOf(routeData.budgetRange["start"]?.toString() ?: "0") }
    var budgetEnd by remember { mutableStateOf(routeData.budgetRange["end"]?.toString() ?: "0") }
    var convenienceRoute by remember { mutableStateOf(routeData.convenience) }
    var selectedTypeOfTransport by remember { mutableStateOf(routeData.typeOfTransport) }
    var typesOfRoute by remember { mutableStateOf(routeData.typesOfRoute) }


    val type by remember { mutableStateOf(routeData.typesOfRoute) }
    val selectedTypesOfRoute = remember { mutableStateListOf<String>().apply { addAll(type) }}

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val convenience by remember { mutableStateOf(routeData.convenience) }
    val selectedConveniences = remember { mutableStateListOf<String>().apply { addAll(convenience) }}


    fun onSavePressed() {
        when {
            selectedCity.isEmpty() -> showToast(context, "Укажите город")
            selectedRouteTime == "День или больше" && exactDays.isEmpty() -> showToast(context, "Укажите количество дней")
            budgetStart.isEmpty() || budgetEnd.isEmpty() -> showToast(context, "Укажите диапазон цен")
            selectedTypesOfRoute.isEmpty() -> showToast(context, "Укажите тип маршрута")
            else -> {
                routeCreationViewModel.updateCity(selectedCity)
                routeCreationViewModel.updateTime(selectedRouteTime)
                if (selectedRouteTime == "День или больше") {
                    routeCreationViewModel.updateDays(exactDays.toInt())
                }
                routeCreationViewModel.updateDifficulty(selectedDifficultyLevel)
                routeCreationViewModel.updatePrice(
                    budgetStart.toIntOrNull() ?: 0,
                    budgetEnd.toIntOrNull() ?: 0
                )
                routeCreationViewModel.updateTransport(selectedTypeOfTransport)
                routeCreationViewModel.updateConveniences(selectedConveniences)
                routeCreationViewModel.updateTypes(selectedTypesOfRoute)
                navController.navigate("RouteDetails")
            }
        }
    }


    LaunchedEffect(budgetStart, budgetEnd) {
        errorMessage = validatePriceRange(budgetStart, budgetEnd)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
            .verticalScroll(rememberScrollState())
    ) {
            Spacer(modifier = Modifier.height(40.dp))

            Header(
                title = "Детали маршрута",
                startIcon = painterResource(id = R.drawable.ic_back3),
                startIconAction = { navController.navigateUp() },
                endIcon = painterResource(id = R.drawable.ic_next3),
                endIconAction = { onSavePressed() }
            )

        Column(
            Modifier.padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            CustomText("Местоположение", 18, R.font.manrope_semibold, 26)
            Spacer(Modifier.height(8.dp))
            CitySelectionField(
                selectedCity = selectedCity,
                onCitySelected = { city -> selectedCity = city }
            )

            Spacer(Modifier.height(16.dp))
            CustomText("Длительность маршрута", 18, R.font.manrope_semibold, 26)
            Spacer(Modifier.height(8.dp))
            DurationSelection(
                selectedRouteTime = selectedRouteTime,
                onRouteTimeSelected = { duration -> selectedRouteTime = duration }
            )


            if (selectedRouteTime == "День или больше") {
                Spacer(Modifier.height(16.dp))
                CustomText("Примерное количество дней", 18, R.font.manrope_semibold, 26)
                Spacer(Modifier.height(8.dp))
                NumberOfDays(
                    days = exactDays,
                    onDaysChanged = { exactDays = it }
                )
            }

            Spacer(Modifier.height(16.dp))
            CustomText("Ценовой диапазон ₽", 18, R.font.manrope_semibold, 26)
            Spacer(Modifier.height(8.dp))
            PriceRoute(
                priceStart = budgetStart,
                priceEnd = budgetEnd,
                onPriceStartChanged = { budgetStart = it },
                onPriceEndChanged = { budgetEnd = it },
                errorMessage = errorMessage
            )

            Spacer(Modifier.height(16.dp))
            CustomText("Сложность маршрута", 18, R.font.manrope_semibold, 26)
            Spacer(Modifier.height(8.dp))
            DifficultyLevelSelector(
                selectedDifficultyLevel = selectedDifficultyLevel,
                onDifficultyLevelSelected = {difficultyLevel -> selectedDifficultyLevel = difficultyLevel }
            )

            Spacer(Modifier.height(16.dp))
            CustomText("Тип маршрута", 18, R.font.manrope_semibold, 26)
            TypeOfRouteSelection(
                selectedTypes = selectedTypesOfRoute.toSet(),
                onTypeSelected = { type, isChecked ->
                    if (isChecked) { selectedTypesOfRoute.add(type) } else { selectedTypesOfRoute.remove(type) }
                    typesOfRoute = selectedTypesOfRoute
                }
            )

            Spacer(Modifier.height(16.dp))
            CustomText("Тип передвижения", 18, R.font.manrope_semibold, 26)
            TypeOfTransportRoute(
                selectedTypeOfTransport = selectedTypeOfTransport,
                onTypeOfTransportSelected = { type ->
                    selectedTypeOfTransport = type
                }
            )

            Spacer(Modifier.height(8.dp))
            CustomText("Удобства", 18, R.font.manrope_semibold, 26)
            ConvenienceSelection(
                selectedConveniences = selectedConveniences.toSet(),
                onConvenienceSelected = { convenience, isChecked ->
                    if (isChecked) { selectedConveniences.add(convenience) } else { selectedConveniences.remove(convenience) }
                    convenienceRoute = selectedConveniences
                }
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}