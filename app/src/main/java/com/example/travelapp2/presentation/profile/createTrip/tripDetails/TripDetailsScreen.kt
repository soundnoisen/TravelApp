package com.example.travelapp2.presentation.profile.createTrip.tripDetails

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.example.travelapp2.data.models.TripFirebase
import com.example.travelapp2.domain.validation.isFirstDateEarlier
import com.example.travelapp2.domain.validation.isValidDate
import com.example.travelapp2.domain.validation.isValidLogicalDate
import com.example.travelapp2.presentation.profile.createTrip.TripCreationViewModel
import com.example.travelapp2.presentation.common.CitySelectionField
import com.example.travelapp2.presentation.common.CustomText
import com.example.travelapp2.presentation.common.DateRangePicker
import com.example.travelapp2.presentation.profile.component.AddPhotoButton
import com.example.travelapp2.presentation.profile.component.CustomField
import com.example.travelapp2.presentation.profile.component.ImageThumbnail
import com.example.travelapp2.presentation.profile.component.OpenImageDialog
import com.example.travelapp2.presentation.profile.createRoute.component.Header
import com.example.travelapp2.presentation.profile.createRoute.component.showToast


@Composable
fun TripDetailsScreen(navController: NavHostController, tripCreationViewModel: TripCreationViewModel) {
    val context = LocalContext.current

    val tripData by tripCreationViewModel.tripData.observeAsState(TripFirebase())

    var titleTrip by remember { mutableStateOf(tripData.title) }
    var selectedCity by remember { mutableStateOf(tripData.city) }
    var startDate by remember { mutableStateOf(tripData.startDate) }
    var endDate by remember { mutableStateOf(tripData.endDate) }
    var descriptionTrip by remember { mutableStateOf(tripData.description) }
    val photos by remember { mutableStateOf(tripData.photos) }
    var isPublic by remember { mutableStateOf(tripData.isPublic) }
    val errorMessage by remember { mutableStateOf<String?>(null) }


    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents()) { uris ->
        selectedImages = uris + selectedImages
    }
    var selectedImage by remember { mutableStateOf<Uri?>(null) }


    fun onSavePressed() {
        when {
            titleTrip.isEmpty() -> {
                showToast(context, "Напишите название путешествия")
            }
            startDate.isEmpty() || endDate.isEmpty() -> {
                showToast(context, "Введите даты путешествия")
            }
            selectedCity.isEmpty() -> {
                showToast(context, "Укажите город")
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
                    tripCreationViewModel.updateTitle(titleTrip)
                    tripCreationViewModel.updateCity(selectedCity)
                    tripCreationViewModel.updateDescription(descriptionTrip)
                    tripCreationViewModel.updateDates(startDate, endDate)
                    tripCreationViewModel.updateIsPublic(isPublic)
                    tripCreationViewModel.updateSelectedImages(selectedImages)

                    navController.navigate("TripPoints")
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

            CustomText("Название путешествия", 18, R.font.manrope_semibold, 26)
            Spacer(Modifier.height(8.dp))
            CustomField(
                text = titleTrip,
                placeholder = "Введите название путешествия",
                onTextChanged = {input -> titleTrip = input},
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

            Spacer(modifier = Modifier.height(16.dp))
            CustomText("Описание", 18, R.font.manrope_semibold, 26)
            Spacer(Modifier.height(8.dp))
            CustomField(
                text = descriptionTrip,
                placeholder = "Опишите ваше путешествие",
                onTextChanged = {input -> descriptionTrip = input},
            )

            Spacer(Modifier.height(16.dp))
            CustomText("Фотографии маршрута", 18, R.font.manrope_semibold, 26)
            Spacer(Modifier.height(8.dp))

            if (photos.isNotEmpty() || selectedImages.isNotEmpty()) {
                val allImages = photos + selectedImages.map { it.toString() }

                LazyRow(modifier = Modifier.padding(bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    items(allImages) { imageUri ->
                        ImageThumbnail(uri = Uri.parse(imageUri)) {
                            selectedImage = it
                        }
                    }
                    item {
                        AddPhotoButton { launcher.launch("image/*") }
                    }
                }
            } else {
                AddPhotoButton { launcher.launch("image/*") }
            }

            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText("Сделать маршрут публичным", 18, R.font.manrope_semibold, 26)
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
        selectedImage?.let { uri ->
            OpenImageDialog(
                uri = uri,
                onDismiss = { selectedImage = null },
                onDelete = { deletedUri ->
                    selectedImages = selectedImages.filter { it != deletedUri }
                    selectedImage = null
                }
            )
        }

    }
}