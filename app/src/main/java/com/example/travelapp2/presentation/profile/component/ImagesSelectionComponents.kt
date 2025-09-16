package com.example.travelapp2.presentation.profile.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberImagePainter
import com.example.travelapp2.R


@Composable
fun ImageThumbnail(uri: Uri, onClick: (Uri) -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clickable { onClick(uri) }
            .padding(end = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberImagePainter(uri),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}



@Composable
fun OpenImageDialog(
    uri: Uri,
    onDismiss: () -> Unit,
    onDelete: (Uri) -> Unit = { onDismiss() }
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f)) // затемнение фона
        ) {
            Image(
                painter = rememberImagePainter(uri),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onDismiss() },
                contentScale = ContentScale.Fit
            )

            IconButton(
                onClick = { onDelete(uri) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.6f), shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить изображение",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun AddPhotoButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .shadow(
                8.dp,
                shape = RoundedCornerShape(60.dp),
                spotColor = Color.Black.copy(alpha = 0.02f)
            )
            .background(Color.White, shape = RoundedCornerShape(60.dp))
            .size(60.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add_photo),
            contentDescription = null,
            tint = colorResource(id = R.color.main)
        )

    }
}