package com.example.travelapp2.presentation.profile.profileInformation.component

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.travelapp2.R
import com.example.travelapp2.emptyUserPhoto


@Composable
fun UserEditPhoto(
    selectedPhotoUri: Uri?,
    photoUrl: String,
    onPhotoSelected: (Uri) -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { onPhotoSelected(it) }
        }
    )

    val imageToDisplay = when {
        selectedPhotoUri != null -> selectedPhotoUri.toString()
        photoUrl.isNotEmpty() -> photoUrl
        else -> emptyUserPhoto
    }

    Box(
        modifier = Modifier.size(100.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageToDisplay)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentScale = ContentScale.Crop
        )

        IconButton(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White, CircleShape)
                .border(1.dp, Color.Gray, CircleShape)
                .padding(2.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Редактировать фото",
                tint = colorResource(id = R.color.main),
            )
        }
    }
}