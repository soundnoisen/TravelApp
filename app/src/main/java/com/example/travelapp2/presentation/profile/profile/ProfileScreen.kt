package com.example.travelapp2.presentation.profile.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.travelapp2.R
import com.example.travelapp2.emptyContentPhoto
import com.example.travelapp2.presentation.viewmodels.TripFirebaseViewModel
import com.example.travelapp2.presentation.common.CustomButton
import com.example.travelapp2.presentation.profile.profile.component.ProfileInformation
import com.example.travelapp2.presentation.profile.profile.component.ProfileName
import com.example.travelapp2.presentation.profile.profile.component.ProfileTabRow
import com.example.travelapp2.presentation.profile.profile.component.UserPhoto
import com.example.travelapp2.presentation.viewmodels.RouteFirebaseViewModel
import com.example.travelapp2.presentation.viewmodels.UserPreferencesViewModel


@Composable
fun ProfileScreen(
    navController: NavHostController,
    userProfileViewModel: UserProfileViewModel,
    tripFirebaseViewModel: TripFirebaseViewModel,
    routeFirebaseViewModel: RouteFirebaseViewModel,
    userId: String,
    userPreferencesViewModel: UserPreferencesViewModel
) {

    LaunchedEffect(Unit) {
        userProfileViewModel.clearProfile()
    }

    LaunchedEffect(userId) {
        routeFirebaseViewModel.getRoutesByUserId(userId)
        routeFirebaseViewModel.getPublicRoutesUser(userId)
        tripFirebaseViewModel.getTripsByUserId(userId)
        tripFirebaseViewModel.getPublicTripsByUserId(userId)

        userProfileViewModel.setUserId(userId)

    }

    val routes by routeFirebaseViewModel.userRoutes.observeAsState(emptyList())
    val publicRoutesUser by routeFirebaseViewModel.publicRoutesUser.observeAsState(emptyList())

    val trips by tripFirebaseViewModel.userTrips.observeAsState(emptyList())
    val publicTripsUser by tripFirebaseViewModel.userPublicTrips.observeAsState(emptyList())

    val profile by userProfileViewModel.profile.observeAsState()

    var id by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var numberOfPosts by remember { mutableIntStateOf(0) }
    var numberOfSubscribers by remember { mutableIntStateOf(0) }
    var numberOfSubscriptions by remember { mutableIntStateOf(0) }
    var photo by remember { mutableStateOf("") }

    var myProfile by remember { mutableStateOf(false) }


    LaunchedEffect(profile) {
        profile?.let {
            id = it.userId
            name = it.displayName.ifEmpty { it.email }
            numberOfPosts = trips.size + routes.size
            numberOfSubscribers = it.subscribersId.size
            numberOfSubscriptions = it.subscriptionsId.size
            photo = it.photoUrl
            myProfile = userId == userProfileViewModel.currentUser
        }
    }

    LaunchedEffect(trips, routes) {
        numberOfPosts = routes.size + trips.size
    }


    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val tabs = listOf("Путешествия", "Маршруты")

    if (profile == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                color = colorResource(id = R.color.main)
            )
        }
    } else {
        Column(
            Modifier
                .testTag("ProfileScreen")
                .fillMaxSize()
                .background(colorResource(id = R.color.background),),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Spacer(modifier = Modifier.padding(top = 40.dp))
            ProfileName(navController, name, id, myProfile)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.background))
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        UserPhoto(photo)
                        Spacer(modifier = Modifier.height(16.dp))
                        ProfileInformation(
                            numberOfPosts,
                            numberOfSubscribers,
                            numberOfSubscriptions,
                            navController,
                            id
                        )
                        Spacer(modifier = Modifier.padding(top = 16.dp))

                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            if (myProfile) {
                                when (selectedTabIndex) {
                                    0 -> CustomButton(
                                        onClick = { navController.navigate("TripDetails") },
                                        buttonText = "Добавить путешествие"
                                    )
                                    1 -> CustomButton(
                                        onClick = { navController.navigate("RoutePoints") },
                                        buttonText = "Добавить маршрут"
                                    )
                                }
                            } else {
                                val isSubscribed = profile?.subscribersId?.contains(userProfileViewModel.currentUser) == true
                                CustomButton(
                                    onClick = {
                                        if (isSubscribed) {
                                            userProfileViewModel.removeSubscription(userId)
                                        } else {
                                            userProfileViewModel.addSubscription(userId)
                                        }
                                    },
                                    color = if (isSubscribed) R.color.next else R.color.main,
                                    colorText = if (isSubscribed) Color.Black else Color.White,
                                    buttonText = if (isSubscribed) "Отписаться" else "Подписаться"
                                )
                            }

                        }
                    }
                }

                stickyHeader {
                    ProfileTabRow(
                        tabs = tabs,
                        selectedTabIndex = selectedTabIndex,
                        modifier = Modifier.testTag("trips_tab"),
                        modifier2 = Modifier.testTag("routes_tab"),
                        onTabSelected = { selectedTabIndex = it }
                    )
                }
                
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
                

                when (selectedTabIndex) {
                    0 -> items(if (myProfile) trips.chunked(3) else publicTripsUser.chunked(3)) { rowTrips ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (trip in rowTrips) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                ) {
                                    MiniProfileCard(
                                        photo = trip.photos.firstOrNull(),
                                        onClick = {
                                            navController.navigate("TripScreen/${trip.id}")
                                        }
                                    )
                                }
                            }
                            repeat(3 - rowTrips.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    1 -> items(if (myProfile) routes.chunked(3) else publicRoutesUser.chunked(3)) { rowRoutes ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (route in rowRoutes) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                ) {
                                    MiniProfileCard(
                                        photo = route.photos.firstOrNull(),
                                        onClick = {
                                            navController.navigate("RouteScreen/${route.id}")
                                            userPreferencesViewModel.trackAndSaveRouteView(route)
                                        }
                                    )
                                }
                            }
                            repeat(3 - rowRoutes.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.padding(top = 16.dp)) }
            }
        }
    }
}

@Composable
fun MiniProfileCard(
    photo: String?,
    onClick: () -> Unit
) {
    val imageUrl = if (photo.isNullOrEmpty()) {
        emptyContentPhoto
    } else {
        photo
    }

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .background(colorResource(id = R.color.alert_line))
            .clickable(onClick = onClick),
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(0.dp)

    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

