package com.example.travelapp2.presentation.profile.profileSub.component

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.travelapp2.R
import com.example.travelapp2.data.models.UserProfileFirebase
import com.example.travelapp2.emptyUserPhoto


@Composable
fun UserListItem(
    user: UserProfileFirebase,
    onClick: () -> Unit,
    onRemoveClick: () -> Unit,
    showRemoveButton: Boolean
) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showDialog) {
        AlertDialog(
            shape = RoundedCornerShape(10.dp),
            onDismissRequest = { showDialog = false },
            title = { Text(text ="Удалить пользователя?", fontSize = 18.sp, color = Color.Black) },
            text = { Text("Вы уверены, что хотите удалить этого пользователя из списка?", color = Color.Gray) },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    onRemoveClick()
                    Toast.makeText(context, "Пользователь удалён", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Удалить", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Отмена", color = Color.Gray)
                }
            },
            containerColor = colorResource(id = R.color.background)
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() })
            .padding(start =  16.dp)
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = user.photoUrl.ifEmpty {
                emptyUserPhoto
            },
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = user.displayName.ifEmpty { user.email },
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            if (user.bio.isNotEmpty()) {
                Text(
                    text = user.bio,
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
        }

        if (showRemoveButton) {
            IconButton(onClick = { showDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Удалить",
                    tint = Color.Red
                )
            }
        }
    }
}