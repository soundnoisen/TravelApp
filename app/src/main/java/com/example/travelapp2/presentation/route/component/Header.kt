package com.example.travelapp2.presentation.route.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.travelapp2.R

@Composable
fun Header(
    navController: NavHostController,
    tint: Color,
    isSaved: Boolean,
    onSave: () -> Unit,
    showSaveButton: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = { navController.navigateUp() },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back3),
                tint = tint,
                contentDescription = null
            )
        }

        if (showSaveButton) {
            IconButton(
                onClick = onSave,
            ) {
                Icon(
                    modifier = Modifier.size(22.dp),
                    painter = painterResource(
                        id = if (isSaved) R.drawable.ic_save else R.drawable.ic_unsave
                    ),
                    tint = tint,
                    contentDescription = null
                )
            }
        }
    }
}
