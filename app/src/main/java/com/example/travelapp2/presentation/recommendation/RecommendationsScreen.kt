package com.example.travelapp2.presentation.recommendation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.travelapp2.R
import com.example.travelapp2.data.network.Route
import com.example.travelapp2.presentation.common.CustomButton
import com.example.travelapp2.presentation.profile.createRoute.RouteCreationViewModel
import com.example.travelapp2.presentation.profile.createRoute.component.showToast
import com.example.travelapp2.presentation.recommendation.component.HeaderRecommendation
import com.example.travelapp2.presentation.recommendation.component.RouteItem
import com.example.travelapp2.presentation.viewmodels.UserRecommendationViewModel
import com.example.travelapp2.presentation.viewmodels.LocationViewModel
import com.example.travelapp2.presentation.viewmodels.RouteFirebaseViewModel

@Composable
fun RecommendationsScreen(
    navHostController: NavHostController,
    userRecommendationViewModel: UserRecommendationViewModel,
    locationViewModel: LocationViewModel,
    routeCreationViewModel: RouteCreationViewModel,
    routeFirebaseViewModel: RouteFirebaseViewModel
) {
    val city by locationViewModel.city.collectAsState(initial = "Местоположение не определено")
    val coordinatesState by locationViewModel.coordinates.collectAsState()
    val coordinatesCity by locationViewModel.cityCoordinates.collectAsState()
    val context = LocalContext.current
    val routes by userRecommendationViewModel.routes.observeAsState(emptyList())
    val isLoading by userRecommendationViewModel.isLoading.collectAsState()
    val start by userRecommendationViewModel.start.collectAsState()

    fun saveRoute(route: Route) {
        val routeFirebase = routeFirebaseViewModel.routeConversion(city, route)
        routeCreationViewModel.saveGenRouteToFirebase(routeFirebase,
            onSuccess = {
                showToast(context, "Маршрут успешно сохранен!")
            },
            onFailure = { exception ->
                showToast(context, "Ошибка при сохранении маршрута: ${exception.message}")
            }
        )
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
    ) {

        HeaderRecommendation(navHostController)

        if (!start) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.ic_bot),
                        contentDescription = null,
                        tint = colorResource(id = R.color.main)
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                    Text(
                        text = "Привет! Я готов сгенерировать маршруты, которые тебе могут понравятся! Приступим?",
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = Font(R.font.manrope_semibold).toFontFamily(),
                        fontSize = 18.sp,
                        style = LocalTextStyle.current.copy(lineHeight = 30.sp),
                        modifier = Modifier.width(350.dp),
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                    CustomButton(
                        onClick = {
                            if (userRecommendationViewModel.useCurrentLocation) {
                                if (coordinatesState != null && coordinatesCity != null) {
                                    userRecommendationViewModel.start.value = true
                                    userRecommendationViewModel.getGeneratedRoutes(
                                        coordinatesCity!!.first,
                                        coordinatesCity!!.second,
                                        coordinatesState!!.first,
                                        coordinatesState!!.second
                                    )
                                    Log.i(
                                        "UserRecommendationVM",
                                        "${coordinatesState!!.first}" + "${coordinatesState!!.second}"
                                    )
                                    userRecommendationViewModel.isLoading.value = true
                                } else {
                                    showToast(context, "Не удалось получить Вашу геолокацию.")
                                }
                            } else {
                                userRecommendationViewModel.start.value = true
                                userRecommendationViewModel.getGeneratedRoutes(
                                    userRecommendationViewModel.region!!.latitude,
                                    userRecommendationViewModel.region!!.longitude,
                                    userRecommendationViewModel.region!!.latitude,
                                    userRecommendationViewModel.region!!.longitude,
                                )
                                Log.i(
                                    "UserRecommendationVM",
                                    "${coordinatesState!!.first}" + "${coordinatesState!!.second}"
                                )
                                userRecommendationViewModel.isLoading.value = true
                            }
                        },
                        buttonText = "Начать",
                        modifier = Modifier.width(160.dp)
                    )
                }
            }
        } else {
            if (isLoading && routes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = colorResource(id = R.color.main)
                    )
                }
            }

            if (routes.isNotEmpty()) {
                Column {
                    Spacer(modifier = Modifier.height(86.dp))
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        itemsIndexed(routes) { index, route ->
                            RouteItem(
                                index,
                                route,
                                city,
                                navHostController,
                                routeFirebaseViewModel
                            ) { saveRoute(route) }
                        }
                        item {
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                IconButton(onClick = {
                                    userRecommendationViewModel.clearRoutes()
                                    if (userRecommendationViewModel.useCurrentLocation) {
                                        userRecommendationViewModel.getGeneratedRoutes(
                                            coordinatesCity!!.first,
                                            coordinatesCity!!.second,
                                            coordinatesState!!.first,
                                            coordinatesState!!.second,)
                                    } else {
                                        userRecommendationViewModel.getGeneratedRoutes(
                                            userRecommendationViewModel.region!!.latitude,
                                            userRecommendationViewModel.region!!.longitude,
                                            userRecommendationViewModel.region!!.latitude,
                                            userRecommendationViewModel.region!!.longitude,)
                                    }
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.round_replay_24),
                                        contentDescription = null,
                                        Modifier.size(32.dp),
                                        tint = colorResource(
                                            id = R.color.main
                                        )
                                    )
                                }
                            }
                        }
                        item { 
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            if (!isLoading && routes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Нет доступных маршрутов.",
                        fontSize = 14.sp,
                        fontFamily = Font(R.font.inter_regular).toFontFamily(),
                        color = colorResource(id = R.color.hint)
                    )
                }
            }
        }
    }
}



