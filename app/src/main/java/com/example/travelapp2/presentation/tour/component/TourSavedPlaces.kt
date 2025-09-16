package com.example.travelapp2.presentation.tour.component

import androidx.compose.runtime.Composable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.travelapp2.R
import com.example.travelapp2.data.models.PlaceFirebase
import com.example.travelapp2.data.models.TourFirebase
import com.example.travelapp2.presentation.viewmodels.CategorySearchViewModel
import com.example.travelapp2.presentation.viewmodels.GIS2ViewModel
import com.example.travelapp2.presentation.viewmodels.ToursViewModel


@Composable
fun TourSavedPlaces(
    placesByCategory: Map<String, List<PlaceFirebase>>,
    currentTour: TourFirebase,
    toursViewModel: ToursViewModel,
    navController: NavHostController,
    mapViewModel: GIS2ViewModel,
    categorySearchViewModel: CategorySearchViewModel
) {
    val expandedStates = remember {
        mutableStateMapOf(
            "attractions" to false,
            "nature" to false,
            "cultural" to false,
            "restaurants" to false,
        )
    }

    val categoryMap = mapOf(
        "attractions" to Triple("Достопримечательности", Icons.Default.LocationOn, Color(0xFFFFF3E0)),
        "nature" to Triple("Природа и прогулки", Icons.Default.Terrain, Color(0xFFE8F5E9)),
        "cultural" to Triple("Культурные места", Icons.Default.Museum, Color(0xFFF3E5F5)),
        "restaurants" to Triple("Рестораны и кафе", Icons.Default.Restaurant, Color(0xFFE1F5FE)),
        )

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        categoryMap.forEach { (key, value) ->
            val (title, icon, bgColor) = value
            val items = placesByCategory[key] ?: emptyList()

            ExpandableSection(
                title = title,
                icon = icon,
                itemCount = items.size,
                items = items,
                expanded = expandedStates[key] ?: false,
                onToggle = { expandedStates[key] = !(expandedStates[key] ?: false) },
                onRemoveItem = { itemName ->
                    val updatedPlaces = currentTour.savedPlacesId[key]?.toMutableList()?.apply {
                        removeIf { id -> placesByCategory[key]?.find { it.name == itemName }?.id == id }
                    } ?: return@ExpandableSection

                    val updatedSavedPlaces = currentTour.savedPlacesId.toMutableMap().apply {
                        put(key, updatedPlaces)
                    }

                    toursViewModel.updateSavedPlaces(currentTour.id, updatedSavedPlaces)
                },
                onAddItem = {
                    navController.navigate("placePickerScreen/$key/${currentTour.id}")
                },
                mapViewModel,
                categorySearchViewModel,
                navController
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}





@Composable
fun ExpandableSection(
    title: String,
    icon: ImageVector,
    itemCount: Int,
    items: List<PlaceFirebase>,
    expanded: Boolean,
    onToggle: () -> Unit,
    onRemoveItem: (String) -> Unit,
    onAddItem: () -> Unit,
    mapViewModel: GIS2ViewModel,
    categorySearchViewModel: CategorySearchViewModel,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                16.dp,
                shape = RoundedCornerShape(10.dp),
                spotColor = Color.Black.copy(alpha = 0.06f)
            )
            .background(Color.White, shape = RoundedCornerShape(10.dp)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { onToggle() }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = colorResource(id = R.color.main))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$title ($itemCount)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = Color.Gray
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items.forEach { item ->
                    PlaceCard(item,
                        onClickDelete = {onRemoveItem(item.name)},
                        onClick = {
                            if (item.fromApi == "2gis") {
                                mapViewModel.updatePlace(item)
                                navController.navigate("GisPlaceScreen")
                            }
                            if (item.fromApi == "geoapify") {
                                categorySearchViewModel.updatePlace(item)
                                navController.navigate("GeoApifyPlaceScreen")
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { onAddItem() },
                    border = BorderStroke(1.dp, colorResource(id = R.color.main)),
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Добавить",
                        tint = colorResource(id = R.color.main)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Добавить место", color = colorResource(id = R.color.main))
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun PlaceCard(place: PlaceFirebase, onClickDelete: () -> Unit, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .height(100.dp)
            .padding(vertical = 4.dp)
            .border(1.dp, colorResource(id = R.color.alert_line), RoundedCornerShape(10.dp))
        ,
        colors = CardDefaults.cardColors(Color.White),
        onClick = onClick
    ) {
        Row(Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
            ) {
                ImagePlaceCard(place.mainPhotoUrl)
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)) {
                Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = place.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onClickDelete) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Удалить",
                            tint = colorResource(id = R.color.hint)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ImagePlaceCard(photo: String) {
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
            .width(100.dp),
        contentScale = ContentScale.Crop
    )
}

