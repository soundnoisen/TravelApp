package com.example.travelapp2.presentation.profile.profile.component

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.travelapp2.R

@Composable
fun LineVerticalProfileData(){
    VerticalDivider(
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp), color = colorResource(id = R.color.hint)
    )
}