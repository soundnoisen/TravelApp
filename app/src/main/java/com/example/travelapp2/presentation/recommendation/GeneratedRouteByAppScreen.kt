package com.example.travelapp2.presentation.route

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavHostController
import com.example.travelapp2.R
import com.example.travelapp2.presentation.place.place2gis.component.ScheduleItem
import com.example.travelapp2.presentation.profile.createRoute.component.Header
import com.example.travelapp2.presentation.profile.profile.UserProfileViewModel
import com.example.travelapp2.presentation.recommendation.component.RouteView
import com.example.travelapp2.presentation.route.component.PrintMap
import com.example.travelapp2.presentation.route.component.StartRoute
import com.example.travelapp2.presentation.route.component.TextTab
import com.example.travelapp2.presentation.route.component.TextView
import com.example.travelapp2.presentation.viewmodels.RouteFirebaseViewModel

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun GeneratedRouteByAppScreen(
    navController: NavHostController,
    routeFirebaseViewModel: RouteFirebaseViewModel,
    routeId: String,
    userProfileViewModel: UserProfileViewModel
) {
    val context = LocalContext.current
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val firebaseRoute by routeFirebaseViewModel.getRouteById(routeId).observeAsState(null)
    val generatedRoute by routeFirebaseViewModel.routeGeneratedByApp.observeAsState()
    val generatedPlaces by routeFirebaseViewModel.routePlacesGeneratedByApp.observeAsState()


    val route = if (routeId.isNotEmpty()) firebaseRoute else generatedRoute

    val country = "Республика Татарстан"

    val minOffset = 86.dp

    var offsetDp by remember { mutableStateOf(0.dp) }


    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val maxOffsetPerTab = when (selectedTabIndex) {
        0 -> 208.dp
        1 -> screenHeight - 16.dp - (route!!.route.size*56.dp) - (route.route.size*16.dp) - 4.dp
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
        if (route == null) {
            CircularProgressIndicator(
                color = colorResource(id = R.color.main)
            )
        } else {
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
                                .background(colorResource(id = R.color.main))
                        )

                        1 -> Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(420.dp)
                        ) {
                            StartRoute(route = route.route)
                        }
                    }

                    Box(modifier = Modifier.padding(top = 40.dp)) {
                        Header(
                            title = "",
                            startIcon = painterResource(id = R.drawable.ic_back3),
                            startIconAction = { navController.navigateUp() },
                            tint = if (selectedTabIndex == 0) Color.White else Color.Black
                        )
                    }


                    when (selectedTabIndex) {
                        0 ->
                            Column(
                                Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 140.dp)
                            ) {
                                TextView(route.title, 20,  FontWeight.SemiBold, if (selectedTabIndex == 0) Color.White else Color.Black)
                                Spacer(modifier = Modifier.height(8.dp))
                                TextView("$country, ${route.city}", 16,  FontWeight.Normal, if (selectedTabIndex == 0) Color.White else Color.Black)
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
                                val tabs = listOf("Расписание", "Маршрут")
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
                                            modifier = Modifier.indication(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = null
                                            )
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
                                            0 -> Column(Modifier.padding(top = 16.dp)) {
                                                
                                                generatedPlaces!!.mapIndexed() { index, place ->
                                                    Column(
                                                        Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 16.dp)
                                                    ) {
                                                        TextView(
                                                            place.name,
                                                            16,
                                                            FontWeight.Normal,
                                                            Color.Black
                                                        )
                                                        Spacer(modifier = Modifier.height(8.dp))
                                                        val openingList = formatOpeningHoursList(place.opening_hours)
                                                        openingList.forEach { (days, time) ->
                                                            ScheduleItem(day = days, hours = time)
                                                        }
                                                        HorizontalDivider(
                                                            modifier = Modifier.padding(
                                                                vertical = 12.dp
                                                            )
                                                        )
                                                    }
                                                }
                                            }
                                            1 -> RouteView(
                                                route = route.route
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




fun formatOpeningHoursList(hours: String): List<Pair<String, String>> {
    val lower = hours.lowercase().trim()

    if (lower == "не указано") return listOf("Часы работы не указаны" to "")
    if (lower == "24/7") return listOf("Круглосуточно" to "Без выходных")

    val dayMap = mapOf(
        "Mo" to "Пн",
        "Tu" to "Вт",
        "We" to "Ср",
        "Th" to "Чт",
        "Fr" to "Пт",
        "Sa" to "Сб",
        "Su" to "Вс"
    )

    return hours.split(";").map { entry ->
        val parts = entry.trim().split(" ")

        return@map when {
            parts.size == 1 && parts[0].matches(Regex("""\d{2}:\d{2}-\d{2}:\d{2}""")) -> {
                "Ежедневно" to parts[0]
            }

            parts.size == 2 && parts[1].lowercase() == "off" -> {
                val days = parts[0].split(",", limit = 5).joinToString(", ") { day ->
                    dayMap[day.trim()] ?: day.trim()
                }
                days to "Выходной"
            }

            else -> {
                val daysRaw = parts[0]
                val time = parts.drop(1).joinToString(" ")

                val daysFormatted = daysRaw.split(",", limit = 10).joinToString(", ") { dayRange ->
                    if (dayRange.contains("-")) {
                        dayRange.split("-").joinToString("–") {
                            dayMap[it.trim()] ?: it
                        }
                    } else {
                        dayMap[dayRange.trim()] ?: dayRange.trim()
                    }
                }

                daysFormatted to time
            }
        }
    }
}


