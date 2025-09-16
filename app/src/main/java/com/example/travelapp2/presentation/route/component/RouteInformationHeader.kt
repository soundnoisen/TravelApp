package com.example.travelapp2.presentation.route.component

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.travelapp2.R
import com.example.travelapp2.data.models.RouteFirebase
import com.example.travelapp2.presentation.profile.createRoute.RouteCreationViewModel
import com.example.travelapp2.presentation.profile.profile.UserProfileViewModel
import com.example.travelapp2.presentation.viewmodels.RouteFirebaseViewModel


@Composable
fun RouteInformationHeader(
    navController: NavHostController,
    isMyRoute: Boolean,
    routeFirebaseViewModel: RouteFirebaseViewModel,
    routeId: String,
    routeCreationViewModel: RouteCreationViewModel,
    route: RouteFirebase,
    userProfileViewModel: UserProfileViewModel,
    isSaved: Boolean,
    selectedTabIndex: Int,
) {
    val context = LocalContext.current
    val color =  if (route.photos.isNotEmpty() && selectedTabIndex == 0) Color.White else Color.Black

    val actions = listOf("Изменить маршрут", "Удалить маршрут")
    var expanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            shape = RoundedCornerShape(10.dp),
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(text ="Удалить маршрут?", fontSize = 18.sp, color = Color.Black) },
            text = { Text("Вы уверены, что хотите удалить этот маршрут? Это действие невозможно отменить.", color = Color.Gray) },
            confirmButton = {
                TextButton(onClick = {
                    routeFirebaseViewModel.deleteRoute(routeId)
                    showDeleteDialog = false
                    navController.popBackStack()
                    Toast.makeText(context, "Маршрут удалён", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Удалить", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Отмена", color = Color.Gray)
                }
            },
            containerColor = colorResource(id = R.color.background)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp)
    ) {
        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back3),
                tint = color,
                contentDescription = "Назад"
            )
        }

        if (isMyRoute) {
            Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu),
                        contentDescription = "Меню",
                        tint = color,
                        modifier = Modifier.size(24.dp)
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    actions.forEach { action ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = action,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            },
                            onClick = {
                                expanded = false
                                when (action) {
                                    "Изменить маршрут" -> {
                                        routeCreationViewModel.setRoute(route)
                                        navController.navigate("RoutePoints")
                                    }

                                    "Удалить маршрут" -> {
                                        showDeleteDialog = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        } else {
            IconButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = {
                    userProfileViewModel.toggleSavedRoute(routeId) { isNowSaved ->
                        val message = if (isNowSaved) "Добавлено в избранное" else "Удалено из избранного"
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                    routeFirebaseViewModel.onRouteSaved(route)
                },
            ) {
                Icon(
                    modifier = Modifier.size(22.dp),
                    painter = painterResource(
                        id = if (isSaved) R.drawable.ic_save else R.drawable.ic_unsave
                    ),
                    tint = color,
                    contentDescription = null
                )
            }
        }
    }
}