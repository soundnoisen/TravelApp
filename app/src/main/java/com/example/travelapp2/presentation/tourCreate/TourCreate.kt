package com.example.travelapp2.presentation.tourCreate

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.travelapp2.R
import com.example.travelapp2.data.models.TourFirebase
import com.example.travelapp2.data.models.TripFirebase
import com.example.travelapp2.domain.validation.isFirstDateEarlier
import com.example.travelapp2.domain.validation.isValidDate
import com.example.travelapp2.domain.validation.isValidLogicalDate
import com.example.travelapp2.presentation.common.CitySelectionField
import com.example.travelapp2.presentation.common.CustomText
import com.example.travelapp2.presentation.common.DateRangePicker
import com.example.travelapp2.presentation.profile.component.AddPhotoButton
import com.example.travelapp2.presentation.profile.component.CustomField
import com.example.travelapp2.presentation.profile.component.ImageThumbnail
import com.example.travelapp2.presentation.profile.component.OpenImageDialog
import com.example.travelapp2.presentation.profile.createRoute.component.Header
import com.example.travelapp2.presentation.profile.createRoute.component.showToast
import com.example.travelapp2.presentation.tour.component.TextFieldNumber

@Composable
fun TourCreationScreen(navController: NavHostController, tourCreationViewModel: TourCreationViewModel) {
    val context = LocalContext.current

    val tourData by tourCreationViewModel.tourData.observeAsState(TourFirebase())

    var title by remember { mutableStateOf(tourData.title) }
    var selectedCity by remember { mutableStateOf(tourData.city) }
    var startDate by remember { mutableStateOf(tourData.startDate) }
    var endDate by remember { mutableStateOf(tourData.endDate) }
    var maxParticipantsText by remember { mutableStateOf(tourData.maxParticipants.toString()) }
    val maxParticipants = maxParticipantsText.toIntOrNull() ?: 0
    var isPublic by remember { mutableStateOf(tourData.public) }
    val errorMessage by remember { mutableStateOf<String?>(null) }

    fun onSavePressed() {
        when {
            title.isEmpty() -> {
                showToast(context, "Напишите название путешествия")
            }
            startDate.isEmpty() || endDate.isEmpty() -> {
                showToast(context, "Введите даты путешествия")
            }
            selectedCity.isEmpty() -> {
                showToast(context, "Укажите город")
            }
            maxParticipants < 2 -> {
                showToast(context, "Минимальное количество участников: 2")
            }
            else -> {
                val isValidStart = isValidDate(startDate)
                val isValidEnd = isValidDate(endDate)
                if (!isValidStart || !isValidEnd ||
                    !isValidLogicalDate(startDate) || !isValidLogicalDate(endDate)
                ) {
                    showToast(context, "Некорректный формат даты.")
                } else if (!isFirstDateEarlier(startDate, endDate)) {
                    showToast(context, "Дата начала не может быть позже даты окончания")
                } else {

                    tourCreationViewModel.updateTitle(title)
                    tourCreationViewModel.updateCity(selectedCity)
                    tourCreationViewModel.updateDates(startDate, endDate)
                    tourCreationViewModel.updateIsPublic(isPublic)
                    tourCreationViewModel.updateMaxParticipants(maxParticipants)

                    tourCreationViewModel.saveTourToFirebase(
                        onSuccess = { showToast(context, "Поездка успешно создана!") },
                        onFailure = { exception ->
                            showToast(context, "Ошибка при создании поездки: ${exception.message}")
                        }
                    )
                    navController.popBackStack()
                }
            }
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Header(
            title = "Описание",
            startIcon = painterResource(id = R.drawable.ic_back3),
            startIconAction = { navController.navigateUp() },
            endIcon = painterResource(id = R.drawable.ic_next3),
            endIconAction = { onSavePressed() } )

        Column(Modifier.padding(horizontal = 16.dp)) {

            Spacer(modifier = Modifier.height(8.dp))

            CustomText("Название поездки", 18, R.font.manrope_semibold, 26)
            Spacer(Modifier.height(8.dp))
            CustomField(
                text = title,
                placeholder = "Введите название поездки",
                onTextChanged = {input -> title = input},
            )

            Spacer(modifier = Modifier.height(16.dp))
            CustomText("Местоположение", 18, R.font.manrope_semibold, 26)
            Spacer(Modifier.height(8.dp))
            CitySelectionField(
                selectedCity = selectedCity,
                onCitySelected = { city -> selectedCity = city }
            )

            Spacer(modifier = Modifier.height(16.dp))
            CustomText("Дата поездки", 18, R.font.manrope_semibold, 26)
            Spacer(Modifier.height(8.dp))

            DateRangePicker(
                startDate = startDate,
                endDate = endDate,
                onStartDateChanged = { startDate = it },
                onEndDateChanged = { endDate = it },
                errorMessage = errorMessage
            )

            Spacer(Modifier.height(16.dp))
            CustomText("Максимальное кол-во участников", 18, R.font.manrope_semibold, 26)
            Spacer(Modifier.height(8.dp))
            TextFieldNumber(
                value = maxParticipantsText,
                onValueChange = { newValue ->
                    if ((newValue.all { it.isDigit() } && newValue.toIntOrNull()?.let { it > 1 } == true) || newValue.isEmpty()) {
                        maxParticipantsText = newValue
                    }
                }
            )


            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText("Сделать поездку публичной", 18, R.font.manrope_semibold, 26)
                Switch(
                    checked = isPublic,
                    onCheckedChange = { newState -> isPublic = newState },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        uncheckedThumbColor = Color.White,
                        checkedTrackColor = colorResource(id = R.color.main),
                        uncheckedTrackColor = colorResource(id = R.color.switch_off),
                        uncheckedBorderColor = Color.Transparent
                    ),
                )
            }
        }
    }
}
