package com.example.travelapp2.presentation.route

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.travelapp2.presentation.common.CustomButton
import com.example.travelapp2.R
import com.example.travelapp2.presentation.common.CustomText
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.lifecycle.ViewModel
import com.example.travelapp2.data.models.RouteFirebase
import com.example.travelapp2.presentation.profile.createRoute.RouteCreationViewModel
import com.example.travelapp2.presentation.profile.profile.UserProfileViewModel
import com.example.travelapp2.presentation.route.component.ImageCarousel
import com.example.travelapp2.presentation.route.component.PrintMap
import com.example.travelapp2.presentation.route.component.RouteInformationHeader
import com.example.travelapp2.presentation.route.component.RoutePointsView
import com.example.travelapp2.presentation.route.component.ShowMap
import com.example.travelapp2.presentation.route.component.StartRoute
import com.example.travelapp2.presentation.route.component.TextDescription
import com.example.travelapp2.presentation.route.component.TextTab
import com.example.travelapp2.presentation.route.component.TextView

import com.example.travelapp2.presentation.viewmodels.RouteFirebaseViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView



@SuppressLint("UseOfNonLambdaOffsetOverload", "ConfigurationScreenWidthHeight")
@Composable
fun RouteScreen(
    navController: NavHostController,
    routeFirebaseViewModel: RouteFirebaseViewModel,
    routeId: String,
    userProfileViewModel: UserProfileViewModel,
    routeCreationViewModel: RouteCreationViewModel,
) {
    val userProfile by userProfileViewModel.profile.observeAsState()
    val isSaved = remember(userProfile, routeId) {
        userProfile?.savedRoutesId?.contains(routeId) ?: false
    }

    val route by routeFirebaseViewModel.getRouteById(routeId).observeAsState(null)
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val country = "Республика Татарстан"

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    var boxOffset by remember { mutableStateOf(400.dp) }
    val minOffset = 86.dp
    val maxOffset = 400.dp

    var upperPartHeight by remember { mutableStateOf(420.dp) }

    val tabsHeight = 40.dp
    val buttonHeight = 56.dp
    val additionalPadding = 16.dp
    val menuHeight = additionalPadding*2 + tabsHeight + buttonHeight

    LaunchedEffect(selectedTabIndex) {
        route?.let {
            boxOffset = if (selectedTabIndex == 2) (screenHeight - menuHeight) else if (selectedTabIndex == 1) (screenHeight - additionalPadding - (route!!.route.size*56.dp) - (route!!.route.size*16.dp) - 4.dp) else 400.dp
            upperPartHeight = if (selectedTabIndex == 2) (screenHeight - menuHeight + 20.dp) else 420.dp
        }
    }

    val animatedBoxOffset by animateDpAsState(
        targetValue = boxOffset,
        animationSpec = tween(durationMillis = 300)
    )

    if (route == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                color = colorResource(id = R.color.main)
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .testTag("route")
                .background(colorResource(id = R.color.background))
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                when (selectedTabIndex) {
                    0 -> ImageCarousel(Modifier.height(upperPartHeight), route!!.photos, 60.dp)
                    1 -> Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(upperPartHeight)
                    ) {
                        PrintMap(route!!.route)
                    }

                    2 -> Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(upperPartHeight)
                    ) {
                        StartRoute(route!!.route)
                    }
                }


                val isMyRoute = route!!.userId == userProfileViewModel.currentUser


                RouteInformationHeader(navController, isMyRoute, routeFirebaseViewModel, routeId, routeCreationViewModel, route!!, userProfileViewModel, isSaved, selectedTabIndex)

                when (selectedTabIndex) {
                    0 -> Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 400.dp - 42.dp - additionalPadding,
                                start = 16.dp
                            )
                    ) {
                            route?.let {
                                TextView(
                                    it.title,
                                    20,
                                    FontWeight.SemiBold,
                                    if (route!!.photos.isEmpty()) Color.Black else Color.White
                                )
                            }
                            TextView(
                                "$country, ${route!!.city}",
                                16,
                                FontWeight.Normal,
                                if (route!!.photos.isEmpty()) Color.Black else Color.White
                            )
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
                            .then(
                                if (selectedTabIndex == 2) {
                                    Modifier
                                } else {
                                    Modifier.draggable(
                                        orientation = Orientation.Vertical,
                                        state = rememberDraggableState { delta ->
                                            val newOffset = boxOffset + delta.dp
                                            boxOffset = newOffset.coerceIn(minOffset, maxOffset)
                                        },
                                        onDragStopped = {
                                            boxOffset =
                                                if (boxOffset > (maxOffset / 2)) maxOffset else minOffset
                                        }
                                    )
                                }
                            )
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {

                            val tabs = listOf("Описание", "Маршрут", "Локация")
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
                                    .padding(horizontal = 16.dp)
                                    .weight(1f)
                            ) {
                                Column {
                                    when (selectedTabIndex) {
                                        0 -> route?.let {
                                            TextDescription(route!!)
                                        }

                                        1 -> route?.let {
                                            RoutePointsView(route!!.route)
                                        }
                                        2 -> CustomButton(onClick = { /*TODO*/ }, buttonText = "Начать маршрут", modifier = Modifier.padding(top = 16.dp))
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
