package com.example.travelapp2.presentation.tour

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavHostController
import com.example.travelapp2.R
import com.example.travelapp2.presentation.map.component.CustomButton
import com.example.travelapp2.presentation.profile.profile.UserProfileViewModel
import com.example.travelapp2.presentation.route.component.PrintMap
import com.example.travelapp2.presentation.route.component.TextTab
import com.example.travelapp2.presentation.route.component.TextView
import com.example.travelapp2.presentation.tour.component.TourDate
import com.example.travelapp2.presentation.tour.component.TourHeader
import com.example.travelapp2.presentation.tour.component.TourInfo
import com.example.travelapp2.presentation.tour.component.TourLocation2
import com.example.travelapp2.presentation.tour.component.TourParticipants
import com.example.travelapp2.presentation.tour.component.TourRoute
import com.example.travelapp2.presentation.tour.component.TourSavedPlaces
import com.example.travelapp2.presentation.tourCreate.TourCreationViewModel
import com.example.travelapp2.presentation.viewmodels.CategorySearchViewModel
import com.example.travelapp2.presentation.viewmodels.GIS2ViewModel
import com.example.travelapp2.presentation.viewmodels.ToursViewModel

@SuppressLint("SuspiciousIndentation")
@Composable
fun TourScreen(
    navController: NavHostController,
    toursViewModel: ToursViewModel,
    tourId: String,
    userProfileViewModel: UserProfileViewModel,
    chatViewModel: ChatViewModel,
    tourCreationViewModel: TourCreationViewModel,
    mapViewModel: GIS2ViewModel,
    categorySearchViewModel: CategorySearchViewModel
) {
    LaunchedEffect(tourId) {
        toursViewModel.listenToTourById(tourId)
        toursViewModel.listenToParticipants(tourId)
        userProfileViewModel.getUserProfile()
    }

    val tour by toursViewModel.foundTour.observeAsState()
    val userProfile by userProfileViewModel.profile.observeAsState()
    val currentUserId = userProfile?.userId ?: ""

    val isAdmin = tour?.adminId == currentUserId
    val isParticipant = tour?.participants?.contains(currentUserId) == true
    val isRequestSent = tour?.requests?.contains(currentUserId) == true


    val avatars by toursViewModel.participantAvatars.observeAsState(emptyList())

    val placesByCategory by toursViewModel.placesByCategory.observeAsState(emptyMap())


    LaunchedEffect(tour) {
        tour?.savedPlacesId?.let {
            toursViewModel.loadPlacesForTour(it)
        }
    }

    var selectedTabIndex by remember { mutableIntStateOf(0) }


    val minOffset = 86.dp

    var offsetDp by remember { mutableStateOf(0.dp) }
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val maxOffsetPerTab = when (selectedTabIndex) {
        0 -> 254.dp
        1 -> screenHeight - 16.dp*2 - (tour!!.route.size*56.dp) - (tour!!.route.size*16.dp) - 4.dp - 50.dp - 8.dp
        2 -> 86.dp
        else -> 16.dp
    }


    LaunchedEffect(selectedTabIndex) {
        offsetDp = maxOffsetPerTab
    }

    val animatedOffset by animateDpAsState(
        targetValue = offsetDp,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (tour == null) {
            CircularProgressIndicator(
                color = colorResource(id = R.color.main)
            )
        } else {
            val currentTour = tour!!

            LaunchedEffect(currentTour.participants) {
                toursViewModel.loadParticipantAvatars(currentTour.participants)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.background))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(animationSpec = tween(300))
                ) {
                    when (selectedTabIndex) {
                        0, 2 -> Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(420.dp)
                        ) {
                            TourInfo(currentTour.photoUrl, Modifier.height(420.dp))
                        }

                        1 -> Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(420.dp)
                        ) {
                            PrintMap(currentTour.route)
                        }

                    }

                    TourHeader(
                        navController,
                        if (selectedTabIndex != 1) Color.White else Color.Black,
                        R.drawable.ic_back3,
                        R.drawable.ic_settings,
                        isAdmin,
                        tourId
                    )

                    when (selectedTabIndex) {
                        0 ->
                            Column(
                                Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 86.dp)
                            ) {
                                TextView(
                                    currentTour.title,
                                    20,
                                    FontWeight.SemiBold,
                                    Color.White
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                TourDate(currentTour.startDate, currentTour.endDate, Color.White)
                                Spacer(modifier = Modifier.height(8.dp))
                                TourLocation2(currentTour.city)
                                Spacer(modifier = Modifier.height(16.dp))
                                TourParticipants(avatars, navController, currentUserId)
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                    }

                    Column {

                        Box(
                            modifier = Modifier
                                .offset(y = animatedOffset)
                                .draggable(
                                    orientation = Orientation.Vertical,
                                    state = rememberDraggableState { delta ->
                                        val newOffset = offsetDp + delta.dp
                                        offsetDp = newOffset.coerceIn(minOffset, maxOffsetPerTab)
                                    },
                                    onDragStopped = {
                                        offsetDp =
                                            if (offsetDp < (maxOffsetPerTab + minOffset) / 2) {
                                                minOffset
                                            } else {
                                                maxOffsetPerTab
                                            }
                                    }
                                )
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                                .background(colorResource(id = R.color.background))
                                .animateContentSize(
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = FastOutSlowInEasing
                                    )
                                )

                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {

                                if (!isParticipant) {
                                    Spacer(modifier = Modifier.height(150.dp))
                                    if (!isRequestSent) {
                                        Text("Для просмотра деталей необходимо стать участником поездки, отправьте заявку и ожидайте ответа!",
                                            fontWeight = FontWeight.Medium,
                                            lineHeight = 24.sp,
                                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                            textAlign = TextAlign.Center,
                                            fontSize = 16.sp
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))

                                        CustomButton(onClick = { toursViewModel.requestToJoinTour(tourId, currentUserId) },
                                            buttonText = "Подать заявку ",
                                            colorButton = R.color.main,
                                            colorText = R.color.white,
                                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                        )
                                    } else {
                                        Text("Заявка подана, ожидайте одобрения.",
                                            fontWeight = FontWeight.Medium,
                                            lineHeight = 20.sp,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Center,
                                            fontSize = 16.sp
                                        )
                                    }
                                } else {

                                val tabs = listOf("Предложения", "Маршрут", "Чат")
                                TabRow(
                                    selectedTabIndex = selectedTabIndex,
                                    contentColor = colorResource(id = R.color.main),
                                    indicator = { tabPositions ->
                                        SecondaryIndicator(
                                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                            color = colorResource(id = R.color.main)
                                        )
                                    },
                                    divider = {
                                        Divider(
                                            color = colorResource(id = R.color.hint),
                                            thickness = 1.dp
                                        )
                                    }
                                ) {
                                    tabs.forEachIndexed { index, title ->
                                        Tab(
                                            selected = selectedTabIndex == index,
                                            onClick = { selectedTabIndex = index },
                                            modifier = Modifier.indication(interactionSource = remember { MutableInteractionSource() }, indication = null)
                                        ) {
                                            val fontFamily =
                                                if (selectedTabIndex == index) R.font.manrope_semibold else R.font.manrope_medium
                                            val color =
                                                if (selectedTabIndex == index) colorResource(id = R.color.main) else colorResource(
                                                    id = R.color.hint
                                                )
                                            TextTab(title, 16,  FontWeight.Normal, 20, color)
                                        }
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                ) {
                                    Column() {
                                            when (selectedTabIndex) {
                                                0 -> tour?.let { currentTour ->
                                                    TourSavedPlaces(
                                                        placesByCategory = placesByCategory,
                                                        currentTour = currentTour,
                                                        toursViewModel = toursViewModel,
                                                        navController = navController,
                                                        mapViewModel,
                                                        categorySearchViewModel
                                                    )
                                                }

                                                1 -> TourRoute(
                                                    route = currentTour.route,
                                                    onDelete = { index ->
                                                        val updated =
                                                            currentTour.route.toMutableList()
                                                                .also { it.removeAt(index) }
                                                        toursViewModel.updateRouteForTour(
                                                            currentTour.id,
                                                            updated
                                                        )
                                                    },
                                                    onAddPlaceClick = {
                                                        navController.navigate("RoutePlacePickerScreen/${currentTour.id}")
                                                    }
                                                )

                                                2 -> GroupChatScreen(
                                                    currentTour.chatId,
                                                    chatViewModel,
                                                    userProfile,
                                                    navController
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}