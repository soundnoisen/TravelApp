package com.example.travelapp2.presentation.profile.profile.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.travelapp2.R

@Composable
fun ProfileName(navController: NavHostController, name: String, userId: String, myProfile: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (!myProfile) {
            IconButton(
                onClick = { navController.navigateUp() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back3),
                    tint = Color.Black,
                    contentDescription = "Назад"
                )
            }
        }

        Text(
            text = name,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center),
            overflow = TextOverflow.Ellipsis,
            fontFamily = Font(R.font.manrope_semibold).toFontFamily(),
            fontSize = 18.sp,
            style = LocalTextStyle.current.copy(lineHeight = 20.sp),
        )

        if (myProfile) {
            IconButton(
                onClick = {
                    navController.navigate("ProfileInformationScreen/$userId")
                },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings),
                    tint = Color.Black,
                    contentDescription = "Настройки"
                )
            }
        }
    }
}
