package com.example.travelapp2.presentation.profile.createTrip.tripPointsPreview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.travelapp2.R
import com.example.travelapp2.data.models.TripFirebase
import com.example.travelapp2.presentation.profile.createTrip.TripCreationViewModel
import com.example.travelapp2.presentation.profile.createRoute.component.Header
import com.example.travelapp2.presentation.profile.createRoute.component.showToast
import com.example.travelapp2.presentation.util.uploadToCloudinary
import com.example.travelapp2.presentation.util.uriToFile
import com.example.travelapp2.presentation.viewmodels.LocationPointFirebaseViewModel
import kotlinx.coroutines.launch


@Composable
fun TripPointsPreviewScreen(
    navController: NavHostController,
    tripCreationViewModel: TripCreationViewModel,
    locationPointFirebaseViewModel: LocationPointFirebaseViewModel
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val tripData by tripCreationViewModel.tripData.observeAsState(TripFirebase())

    val locationsData by locationPointFirebaseViewModel.locations.observeAsState(emptyList())

    fun onSaveRoute() {
        val selectedImages = tripCreationViewModel.selectedImages.value
        coroutineScope.launch {


            val uploadedUrls = selectedImages.mapNotNull { uri ->
                uriToFile(uri, context)?.let { file ->
                    uploadToCloudinary(file)
                }
            }

            val allPhotosUrls = tripData.photos + uploadedUrls
            tripCreationViewModel.updatePhotosUrls(allPhotosUrls)

            val updatedLocations = locationsData.map { location ->
                val selectedImagesForLocation = locationPointFirebaseViewModel.getImagesById(location.id)
                val uploadedLocationUrls = selectedImagesForLocation.mapNotNull { uri ->
                    uriToFile(uri, context)?.let { file ->
                        uploadToCloudinary(file)
                    }
                }
                location.copy(photos = location.photos + uploadedLocationUrls)
            }
            if (updatedLocations.isNotEmpty()) {
                tripCreationViewModel.updateLocations(updatedLocations)
            }
            tripCreationViewModel.saveTripToFirebase(
                onSuccess = { showToast(context, "Путешествие успешно сохранено!") },
                onFailure = { exception ->
                    showToast(context, "Ошибка при сохранении путешествия: ${exception.message}")
                }
            )
            locationPointFirebaseViewModel.clearLocationPoint()
            navController.popBackStack()
            navController.popBackStack()
            navController.popBackStack()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
    ) {
        Column() {
            Spacer(modifier = Modifier.padding(top = 40.dp))

                Header(
                    title = "Точки",
                    startIcon = painterResource(id = R.drawable.ic_back3),
                    startIconAction = { navController.popBackStack() },
                    endIcon = painterResource(id = R.drawable.ic_next3),
                    endIconAction = { onSaveRoute() })


            Spacer(modifier = Modifier.padding(top = 8.dp))

            LazyColumn(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                itemsIndexed(locationsData) { index, point ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .shadow(
                                16.dp,
                                shape = RoundedCornerShape(10.dp),
                                spotColor = Color.Black.copy(alpha = 0.06f)
                            )
                            .background(Color.White, shape = RoundedCornerShape(10.dp))
                            .clickable {
                                navController.navigate("TripPointDetails/${point.id}")
                            }
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        val text = point.name.ifEmpty {
                            point.address
                        }

                        Text(
                            text = "${index+1}. $text",
                            fontSize = 16.sp,
                            color = Color.Black,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontFamily = Font(R.font.inter_regular).toFontFamily()
                        )
                    }
                    Spacer(modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
    }
}