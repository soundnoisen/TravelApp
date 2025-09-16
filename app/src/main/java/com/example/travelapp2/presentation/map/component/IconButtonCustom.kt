package com.example.travelapp2.presentation.map.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun IconButtonCustom(id: Int, onClick: () -> Unit) {
    Box(modifier = Modifier
        .size(56.dp)
        .shadow(
            16.dp,
            shape = RoundedCornerShape(10.dp),
            spotColor = Color.Black.copy(alpha = 0.06f)
        )
        .background(Color.White, shape = RoundedCornerShape(10.dp))
    ) {
        IconButton(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            onClick = onClick ) {
            Icon(
                modifier = Modifier.width(34.dp),
                painter = painterResource(id = id),
                contentDescription = null
            )
        }
    }
}
