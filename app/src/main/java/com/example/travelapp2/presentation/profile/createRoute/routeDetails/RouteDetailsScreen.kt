package com.example.travelapp2.presentation.profile.createRoute.routeDetails

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.travelapp2.data.models.RouteFirebase
import com.example.travelapp2.presentation.profile.component.CustomField
import com.example.travelapp2.presentation.common.CustomText
import com.example.travelapp2.presentation.profile.component.AddPhotoButton
import com.example.travelapp2.presentation.profile.component.ImageThumbnail
import com.example.travelapp2.presentation.profile.component.OpenImageDialog
import com.example.travelapp2.presentation.profile.createRoute.component.Header
import com.example.travelapp2.presentation.profile.createRoute.component.showToast
import com.example.travelapp2.presentation.util.uploadToCloudinary
import com.example.travelapp2.presentation.util.uriToFile
import com.example.travelapp2.presentation.profile.createRoute.RouteCreationViewModel
import kotlinx.coroutines.launch


@Composable
fun RouteDetailsScreen(navController: NavHostController, routeCreationViewModel: RouteCreationViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val routeData by routeCreationViewModel.routeData.observeAsState(RouteFirebase())

    var titleRoute by remember { mutableStateOf(routeData.title) }
    var descriptionRoute by remember { mutableStateOf(routeData.description) }
    var recommendationsRoute by remember { mutableStateOf(routeData.recommendations) }
    var routesPhotos by remember { mutableStateOf(routeData.photos) }
    var isPublic by remember { mutableStateOf(routeData.public) }

    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents()) { uris -> selectedImages = uris + selectedImages }

    var selectedImage by remember { mutableStateOf<Uri?>(null) }


    fun onSaveRoute() {
        if (titleRoute.isEmpty()) { showToast(context, "Укажите название маршрута")
            return
        }
        routeCreationViewModel.updateTitle(titleRoute)
        routeCreationViewModel.updateDescription(descriptionRoute)
        routeCreationViewModel.updateRecommendations(recommendationsRoute)
        routeCreationViewModel.updateIsPublic(isPublic)

        coroutineScope.launch {
            val uploadedUrls = selectedImages.mapNotNull { uri ->
                uriToFile(uri, context)?.let { file ->
                    uploadToCloudinary(file)
                }
            }
            val allPhotosUrls = routesPhotos + uploadedUrls
            routeCreationViewModel.updatePhotosUrls(allPhotosUrls)

            routeCreationViewModel.saveRouteToFirebase(
                onSuccess = {
                    showToast(context, "Маршрут успешно сохранен!")
                },
                onFailure = { exception ->
                    showToast(context, "Ошибка при сохранении маршрута: ${exception.message}")
                }
            )
            navController.popBackStack()
            navController.popBackStack()
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
                title = "Описание маршрута",
                startIcon = painterResource(id = R.drawable.ic_back3),
                startIconAction = { navController.navigateUp() },
                endIcon = painterResource(id = R.drawable.ic_next3),
                endIconAction = { onSaveRoute() })

        Column(
            Modifier.padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            CustomText("Название маршрута", 18, R.font.manrope_semibold, 26)
            Spacer(Modifier.height(8.dp))
            CustomField(
                text = titleRoute,
                placeholder = "Введите название маршрута",
                onTextChanged = {input -> titleRoute = input},
            )

            Spacer(Modifier.height(16.dp))
            CustomText("Описание", 18, R.font.manrope_semibold, 26)
            Spacer(Modifier.height(8.dp))
            CustomField(
                text = descriptionRoute,
                placeholder = "Опишите маршрут",
                onTextChanged = {input -> descriptionRoute = input},
            )


            Spacer(Modifier.height(16.dp))
            CustomText("Рекомендации", 18, R.font.manrope_semibold, 26)
            Spacer(Modifier.height(8.dp))
            CustomField(
                text = recommendationsRoute,
                placeholder = "Добавьте полезные советы для туристов",
                onTextChanged = {input -> recommendationsRoute = input},
            )

            Spacer(Modifier.height(16.dp))
            CustomText("Фотографии маршрута", 18, R.font.manrope_semibold, 26)
            Spacer(Modifier.height(8.dp))

            if (routesPhotos.isNotEmpty() || selectedImages.isNotEmpty()) {
                val allImages = routesPhotos + selectedImages.map { it.toString() }

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