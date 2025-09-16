package com.example.travelapp2.presentation.tour

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.travelapp2.R
import com.example.travelapp2.data.models.PlaceFirebase
import com.example.travelapp2.presentation.profile.createRoute.component.Header
import com.example.travelapp2.presentation.profile.profile.UserProfileViewModel
import com.example.travelapp2.presentation.viewmodels.CategorySearchViewModel
import com.example.travelapp2.presentation.viewmodels.GIS2ViewModel
import com.example.travelapp2.presentation.viewmodels.ToursViewModel

@Composable
fun PlacePickerScreen(
    key: String,
    tourId: String,
    toursViewModel: ToursViewModel,
    userProfileViewModel: UserProfileViewModel,
    navController: NavHostController,
    mapViewModel: GIS2ViewModel,
    categorySearchViewModel: CategorySearchViewModel
) {
    val userProfile by userProfileViewModel.profile.observeAsState()
    val savedPlaces = remember { mutableStateOf<List<PlaceFirebase>>(emptyList()) }
    val selectedPlaceIds = remember { mutableStateOf<Set<String>>(emptySet()) }

    LaunchedEffect(userProfile?.savedPlacesId) {
        val savedIds = userProfile?.savedPlacesId ?: emptyList()
        toursViewModel.loadPlacesByIds(savedIds) { places ->
            savedPlaces.value = places
        }
    }

    Column(Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(40.dp))

        Header(
            title = "Сохраненные места",
            startIcon = painterResource(id = R.drawable.ic_back3),
            startIconAction = { navController.navigateUp() },
            endIcon = painterResource(id = R.drawable.ic_next3),
            endIconAction = {
                if (selectedPlaceIds.value.isNotEmpty()) {
                    toursViewModel.addPlacesToCategory(
                        tourId = tourId,
                        categoryKey = key,
                        placeIds = selectedPlaceIds.value
                    )
                    navController.navigateUp()
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .background(colorResource(id = R.color.background))
        ) {
            items(savedPlaces.value) { place ->
                PlaceCard(
                    place = place,
                    isSelected = selectedPlaceIds.value.contains(place.id),
                    onClick = {
                        if (place.fromApi == "2gis") {
                            mapViewModel.updatePlace(place)
                            navController.navigate("GisPlaceScreen")
                        }
                        if (place.fromApi == "geoapify") {
                            categorySearchViewModel.updatePlace(place)
                            navController.navigate("GeoApifyPlaceScreen")
                        }
                    },
                    onLongClick = {
                        selectedPlaceIds.value = selectedPlaceIds.value.toMutableSet().apply {
                            if (contains(place.id)) remove(place.id) else add(place.id)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaceCard(
    place: PlaceFirebase,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val borderColor = if (isSelected) Color(0xFF448AFF) else Color.Transparent
    val backgroundColor = if (isSelected) Color(0xFFE3F2FD) else Color.White

    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .height(130.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .shadow(
                16.dp,
                shape = RoundedCornerShape(10.dp),
                spotColor = Color.Black.copy(alpha = 0.06f)
            )
            .border(2.dp, borderColor, RoundedCornerShape(10.dp)),
        colors = CardDefaults.cardColors(backgroundColor)
    ) {
        Row(Modifier.fillMaxWidth()) {
            ImagePlace(place.mainPhotoUrl)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {


                    Text(
                        text = place.name,
                        modifier = Modifier
                            .fillMaxWidth(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        style = LocalTextStyle.current.copy(lineHeight = 20.sp),
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                Column(Modifier.fillMaxSize()) {

                    Text(
                        text = "Полный адрес: ${place.address.ifEmpty { "отсутствует" }}",
                        modifier = Modifier
                            .fillMaxWidth(),
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = Font(R.font.manrope_medium).toFontFamily(),
                        style = LocalTextStyle.current.copy(lineHeight = 20.sp),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Комментарий к адресу: ${place.addressComment.ifEmpty { "отсутствует" }}",
                        modifier = Modifier
                            .fillMaxWidth(),
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = Font(R.font.manrope_medium).toFontFamily(),
                        style = LocalTextStyle.current.copy(lineHeight = 20.sp),
                    )
                }
            }
        }
    }
}


@Composable
fun ImagePlace(photo: String) {
    val imageUrl = photo.ifEmpty {
        "https://res.cloudinary.com/daszxuaam/image/upload/v1745003101/ChatGPT_Image_4_%D0%B0%D0%BF%D1%80._2025_%D0%B3._13_32_38_iysdw2.png"
    }

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = null,
        modifier = Modifier
            .fillMaxHeight()
            .width(130.dp),
        contentScale = ContentScale.Crop
    )
}
