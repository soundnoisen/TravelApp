package com.example.travelapp2.presentation.tour.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


@Composable
fun TourHeader(
    navController: NavHostController,
    tint: Color,
    firstIcon: Int,
    secondIcon: Int,
    isAdmin: Boolean,
    tourId: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = {
                navController.navigateUp()
            },
        ) {
            Icon(
                painter = painterResource(id = firstIcon),
                tint = tint,
                contentDescription = null
            )
        }
        if (isAdmin) {
            IconButton(
                onClick = {
                    navController.navigate("TourAdminScreen/$tourId")
                          },
                modifier = Modifier
                    .padding(end = 4.dp)

            ) {
                Icon(
                    painter = painterResource(id = secondIcon),
                    tint = tint,
                    contentDescription = null
                )
            }
        }
    }
}