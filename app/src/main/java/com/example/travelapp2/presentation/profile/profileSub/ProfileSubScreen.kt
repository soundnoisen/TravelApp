package com.example.travelapp2.presentation.profile.profileSub

import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.travelapp2.R
import com.example.travelapp2.data.models.UserProfileFirebase
import com.example.travelapp2.domain.repository.UserProfileRepository
import com.example.travelapp2.emptyUserPhoto
import com.example.travelapp2.presentation.profile.profile.UserProfileViewModel
import com.example.travelapp2.presentation.profile.profileSub.component.ProfileSubHeader
import com.example.travelapp2.presentation.profile.profileSub.component.UserList
import com.example.travelapp2.presentation.route.component.TextTab

@Composable
fun ProfileSubScreen(
    navController: NavHostController,
    userId: String,
    userProfileViewModel: UserProfileViewModel,
    selectedTab: Int
) {
    LaunchedEffect(Unit) {
        userProfileViewModel.clearProfile()
    }

    LaunchedEffect(userId) {
        userProfileViewModel.setUserId(userId)
    }

    val profile by userProfileViewModel.profile.observeAsState()
    val subscriptions by userProfileViewModel.subscriptions.observeAsState(emptyList())
    val subscribers by userProfileViewModel.subscribers.observeAsState(emptyList())

    val isMyProfile = userId == userProfileViewModel.currentUser


    var selectedTabState by remember { mutableStateOf(selectedTab) }

    val tabTitles = listOf("Подписчики", "Подписки")


    if (profile == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                color = colorResource(id = R.color.main)
            )
        }
    } else {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.background))
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            ProfileSubHeader(navController, profile?.displayName?.ifEmpty { profile?.email } ?: "")

            TabRow(
                selectedTabIndex = selectedTabState,
                contentColor = colorResource(id = R.color.main),
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabState]),
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
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabState == index,
                        onClick = { selectedTabState = index }
                    ) {
                        val color =
                            if (selectedTabState == index) colorResource(id = R.color.main) else colorResource(
                                id = R.color.hint
                            )
                        TextTab(title, 16,  FontWeight.Normal, 20, color)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            when (selectedTabState) {
                0 -> UserList(
                    users = subscribers,
                    navController = navController,
                    isMyProfile = isMyProfile,
                    onRemoveClick = { targetUser -> userProfileViewModel.removeSubscriber(targetUser.userId) }
                )

                1 -> UserList(
                    users = subscriptions,
                    navController = navController,
                    isMyProfile = isMyProfile,
                    onRemoveClick = { targetUser ->
                        userProfileViewModel.removeSubscription(
                            targetUser.userId
                        )
                    }
                )
            }
        }
    }
}
















