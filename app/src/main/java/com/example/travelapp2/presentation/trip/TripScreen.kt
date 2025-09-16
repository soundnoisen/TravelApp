package com.example.travelapp2.presentation.trip


import android.widget.Toast
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.travelapp2.R
import com.example.travelapp2.presentation.common.CustomText
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.sp
import com.example.travelapp2.data.models.LocationPointFirebase
import com.example.travelapp2.data.models.TripFirebase
import com.example.travelapp2.presentation.profile.createTrip.TripCreationViewModel
import com.example.travelapp2.presentation.profile.profile.UserProfileViewModel
import com.example.travelapp2.presentation.route.component.ImageCarousel
import com.example.travelapp2.presentation.route.component.TextTab
import com.example.travelapp2.presentation.route.component.TextView
import com.example.travelapp2.presentation.trip.component.CardCarousel
import com.example.travelapp2.presentation.trip.component.MapViewComposable
import com.example.travelapp2.presentation.trip.component.PointInformation
import com.example.travelapp2.presentation.trip.component.TripInformationHeader
import com.example.travelapp2.presentation.viewmodels.LocationPointFirebaseViewModel
import com.example.travelapp2.presentation.viewmodels.TripFirebaseViewModel

@Composable
fun TripScreen(
    navController: NavHostController,
    tripFirebaseViewModel: TripFirebaseViewModel,
    userProfileViewModel: UserProfileViewModel,
    tripId: String,
    tripCreationViewModel: TripCreationViewModel,
    locationPointFirebaseViewModel: LocationPointFirebaseViewModel,
) {

    val trip by tripFirebaseViewModel.getTripById(tripId).observeAsState(null)
    val locations by tripFirebaseViewModel.getLocationsByTripId(tripId).observeAsState(emptyList())


    var selectedIndex by remember { mutableStateOf(0) }

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    var boxOffset by remember { mutableStateOf(400.dp) }
    var isExpanded by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf<LocationPointFirebase?>(null) }


    val minOffset by remember { mutableStateOf(86.dp) }
    var maxOffset by remember { mutableStateOf(400.dp) }

    var upperPartHeight by remember { mutableStateOf(420.dp) }

    val tabsHeight = 40.dp
    val cardHeight = 156.dp
    val additionalPadding = 16.dp
    val menuHeight = additionalPadding*2 + tabsHeight + cardHeight

    LaunchedEffect(isExpanded) {
        if (!isExpanded) {
            coroutineScope.launch {
                listState.animateScrollToItem(selectedIndex)
            }
        }
    }

    LaunchedEffect(selectedTabIndex) {
        val newOffset = if (selectedTabIndex == 1) (screenHeight - menuHeight) else 400.dp
        boxOffset = newOffset
        maxOffset = newOffset
        upperPartHeight = if (selectedTabIndex == 1) (screenHeight - menuHeight + 20.dp) else 420.dp
    }

    LaunchedEffect(isExpanded) {
        boxOffset = if (isExpanded) 160.dp else maxOffset
        upperPartHeight = if (isExpanded) 160.dp else (screenHeight - menuHeight + 20.dp)
    }


    val animatedBoxOffset by animateDpAsState(
        targetValue = boxOffset,
        animationSpec = tween(durationMillis = 300)
    )

    if (trip == null) {
        CircularProgressIndicator(
            color = colorResource(id = R.color.main)
        )
    } else {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.background))
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {

                when (selectedTabIndex) {
                    0 -> ImageCarousel(Modifier.height(upperPartHeight), trip!!.photos, 60.dp)
                    1 -> Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(upperPartHeight)
                    ) {
                        MapViewComposable(locations.sortedBy { it.index }, selectedIndex)
                    }
                }
                val isMyTrip = trip!!.userId == userProfileViewModel.currentUser

                TripInformationHeader(navController, tripFirebaseViewModel, trip!!, tripId, selectedTabIndex, isMyTrip, tripCreationViewModel, locations, locationPointFirebaseViewModel)


                when (selectedTabIndex) {
                    0 -> Column(
                        Modifier.padding(
                            top = 400.dp - 42.dp - additionalPadding,
                            start = 16.dp
                        )
                    ) {
                        TextView(trip!!.title, 20,  FontWeight.SemiBold, if (trip!!.photos.isEmpty()) Color.Black else Color.White)
                        TextView(trip!!.city, 14,  FontWeight.Normal, if (trip!!.photos.isEmpty()) Color.Black else Color.White)
                    }
                }

                Column {
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .background(colorResource(id = R.color.background))
                    )
                    Box(
                        modifier = Modifier
                            .offset(y = animatedBoxOffset)
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                            .background(colorResource(id = R.color.background))
                            .draggable(
                                orientation = Orientation.Vertical,
                                state = rememberDraggableState { delta ->
                                    val newOffset = (boxOffset + delta.dp).coerceIn(minOffset, maxOffset)
                                    boxOffset = newOffset
                                },
                                onDragStopped = {
                                    if (boxOffset < (maxOffset / 2)) {
                                        if (selectedTabIndex == 1) {
                                            isExpanded = true
                                        }
                                        boxOffset = minOffset
                                    } else {
                                        if (selectedTabIndex == 1) {
                                            isExpanded = false
                                        }
                                        boxOffset = maxOffset
                                    }
                                }
                            )


                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            if (isExpanded && selectedLocation != null) {
                                PointInformation(selectedLocation)
                            } else {

                                val tabs = listOf("Описание", "Детали")
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
                                            TextTab(title, 16, FontWeight.Normal, 20, color)
                                        }
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .padding()
                                        .weight(1f)
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    Column {
                                        Spacer(modifier = Modifier.padding(top = 16.dp))
                                        when (selectedTabIndex) {
                                            0 -> trip.let {
                                                Box(modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 16.dp)
                                                ) {
                                                    Text(
                                                        text = it!!.description.ifEmpty { "Описание отсутствует." },
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.Normal,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    )
                                                }
                                            }

                                            1 -> CardCarousel(
                                                locations = locations.sortedBy { it.index },
                                                selectedIndex = selectedIndex,
                                                listState = listState,
                                                onSelectedIndexChanged = { newIndex ->
                                                    selectedIndex = newIndex
                                                },
                                                onCardClick = { location ->
                                                    selectedLocation =
                                                        location
                                                    isExpanded = true
                                                }
                                            )

                                        }
                                        Spacer(modifier = Modifier.padding(top = 16.dp))
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






