package com.example.travelapp2.presentation.tour.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.travelapp2.R
import com.example.travelapp2.data.models.UserProfileFirebase

@Composable
fun UserRow(
    user: UserProfileFirebase,
    isAdmin: Boolean,
    navController: NavHostController,
    onClickTrailingIcon: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(
                onClick = {
                    if (!isAdmin) {
                        navController.navigate("Profile/${user.userId}")
                    }
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.photoUrl.ifEmpty {
                        "https://res.cloudinary.com/daszxuaam/image/upload/v1745168269/img_no_photo_user_ir8f4n.png"
                    })
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = user.displayName.ifEmpty { user.email },
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontFamily = Font(R.font.inter_regular).toFontFamily()
                )
            }
        }

        onClickTrailingIcon?.let {
            Row { onClickTrailingIcon() }
        }
    }
}
