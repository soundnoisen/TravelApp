package com.example.travelapp2.presentation.profile.createTrip.tripPointsDetails

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.travelapp2.R
import com.example.travelapp2.domain.validation.isValidDate
import com.example.travelapp2.domain.validation.isValidLogicalDate
import com.example.travelapp2.presentation.common.CitySelectionField
import com.example.travelapp2.presentation.common.CustomText
import com.example.travelapp2.presentation.common.DateTextField
import com.example.travelapp2.presentation.profile.component.AddPhotoButton
import com.example.travelapp2.presentation.profile.component.CustomField
import com.example.travelapp2.presentation.profile.component.ImageThumbnail
import com.example.travelapp2.presentation.profile.component.OpenImageDialog
import com.example.travelapp2.presentation.profile.createRoute.component.Header
import com.example.travelapp2.presentation.profile.createRoute.component.showToast
import com.example.travelapp2.presentation.profile.createTrip.tripPointsDetails.component.MapLocation
import com.example.travelapp2.presentation.viewmodels.LocationPointFirebaseViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun TripPointDetailsScreen(
    navController: NavHostController,
    locationPointFirebaseViewModel: LocationPointFirebaseViewModel,
    pointId: String
) {
    val context = LocalContext.current

    val location = locationPointFirebaseViewModel.locations.value?.find { it.id == pointId }

    if (location != null) {

        var title by remember { mutableStateOf(location.title ?: "") }
        var description by remember { mutableStateOf(location.description ?: "") }
        var dateLocation by remember { mutableStateOf(location.date ?: "") }
        var selectedCity by remember { mutableStateOf(location.city ?: "") }
        val photos by remember { mutableStateOf(location.photos ?: emptyList()) }

        var selectedImages by remember { mutableStateOf(locationPointFirebaseViewModel.getImagesById(pointId)) }
        val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents()) { uris -> selectedImages = uris + selectedImages }
        var selectedImage by remember { mutableStateOf<Uri?>(null) }


        fun onSavePressed() {
            val isDateValid = isValidDate(dateLocation)
            if (dateLocation.isEmpty()) {
                showToast(context, "Введите дату.")
            } else if (!isDateValid || !isValidLogicalDate(dateLocation)) {
                showToast(context, "Некорректный формат даты.")
            } else {
                val updatedLocation = location.copy(
                    title = title,
                    description = description,
                    date = dateLocation,
                    city = selectedCity
                )
                locationPointFirebaseViewModel.updateLocation(updatedLocation)
                locationPointFirebaseViewModel.updateImagesById(updatedLocation.id, selectedImages)
                navController.popBackStack()
            }
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.background))
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(40.dp))

                Header(
                    title = "Редактирование",
                    startIcon = painterResource(id = R.drawable.round_close_24),
                    startIconAction = { navController.popBackStack() },
                    endIcon = painterResource(id = R.drawable.ic_filters_done),
                    endIconAction = { onSavePressed() } )


            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clipToBounds(),
                contentAlignment = Alignment.Center
            ) {
                MapLocation(location.latitude, location.longitude)
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                CustomText("Название точки", 18, R.font.manrope_semibold, 26)
                Spacer(Modifier.height(8.dp))
                CustomField(
                    text = title,
                    placeholder = "Введите название точки",
                    onTextChanged = { input -> title = input },
                )

                Spacer(modifier = Modifier.height(16.dp))
                CustomText("Местоположение", 18, R.font.manrope_semibold, 26)
                Spacer(Modifier.height(8.dp))
                CitySelectionField(
                    selectedCity = selectedCity,
                    onCitySelected = { city -> selectedCity = city }
                )

                Spacer(modifier = Modifier.height(16.dp))
                CustomText("Дата", 18, R.font.manrope_semibold, 26)
                Spacer(Modifier.height(8.dp))
                DateTextField(
                    value = dateLocation,
                    onValueChange = { input -> dateLocation = input },
                    placeholder = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    modifier = Modifier.width(160.dp)
                )


                Spacer(modifier = Modifier.height(16.dp))
                CustomText("Описание", 18, R.font.manrope_semibold, 26)
                Spacer(Modifier.height(8.dp))
                CustomField(
                    text = description,
                    placeholder = "Добавьте описание точки",
                    onTextChanged = { input -> description = input },
                )


                Spacer(Modifier.height(16.dp))
                CustomText("Фотографии", 18, R.font.manrope_semibold, 26)
                Spacer(Modifier.height(8.dp))

                if (photos.isNotEmpty() || selectedImages.isNotEmpty() ) {
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
}