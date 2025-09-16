package com.example.travelapp2.presentation.profile.profileSub.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.travelapp2.data.models.UserProfileFirebase
import com.example.travelapp2.presentation.profile.profileSub.UserListItem


@Composable
fun UserList(
    users: List<UserProfileFirebase>,
    navController: NavHostController,
    isMyProfile: Boolean,
    onRemoveClick: (UserProfileFirebase) -> Unit
) {
    if (users.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Список пуст", color = Color.Gray)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(users) { user ->
                UserListItem(
                    user = user,
                    onClick = { navController.navigate("Profile/${user.userId}") },
                    onRemoveClick = { onRemoveClick(user) },
                    showRemoveButton = isMyProfile // передаём флаг
                )
            }
        }
    }
}