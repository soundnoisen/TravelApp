package com.example.travelapp2.presentation.profile.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ProfileInformation(
    numberOfPosts: Int,
    numberOfSubscribers: Int,
    numberOfSubscriptions: Int,
    navController: NavHostController,
    userId: String
) {
    Row(modifier = Modifier
        .padding(horizontal = 16.dp)
        .fillMaxWidth()
        .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.SpaceBetween)
    {
        ProfileData(numberOfPosts, "Записей") {}
        LineVerticalProfileData()
        ProfileData(numberOfSubscribers, "Подписчиков") {
            navController.navigate("ProfileSubScreen/${userId}/0")
        }
        LineVerticalProfileData()
        ProfileData(numberOfSubscriptions, "Подписок") {
            navController.navigate("ProfileSubScreen/${userId}/1")
        }

    }
}


