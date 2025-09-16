package com.example.travelapp2.presentation.profile.profileInformation.component

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.travelapp2.R
import com.example.travelapp2.presentation.profile.profile.UserProfileViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun ProfileInformationHeader(
    navController: NavHostController,
    mainNavController: NavHostController,
    userProfileViewModel: UserProfileViewModel,
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val lifecycleOwner = LocalLifecycleOwner.current

    val actions = listOf("Выйти из аккаунта", "Удалить аккаунт")
    var expanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }


    if (showExitDialog) {
        AlertDialog(
            shape = RoundedCornerShape(10.dp),
            onDismissRequest = { showExitDialog = false },
            title = { Text(text ="Хотите выйти из аккаунта?", fontSize = 18.sp, color = Color.Black) },
            text = { Text("Вы уверены, что хотите выйти из аккаунта?", color = Color.Gray) },
            confirmButton = {
                TextButton(onClick = {
                    auth.signOut()

                    val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
                    prefs.edit().remove("is_logged_in").apply()

                    mainNavController.navigate("login") {
                        popUpTo(0)
                    }
                    showExitDialog = false
                }) {
                    Text("Выйти", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Отмена", color = Color.Gray)
                }
            },
            containerColor = colorResource(id = R.color.background)
        )
    }



    if (showDeleteDialog) {
        AlertDialog(
            shape = RoundedCornerShape(10.dp),
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(text ="Удалить аккаунт?", fontSize = 18.sp, color = Color.Black) },
            text = { Text("Вы уверены, что хотите удалить этот аккаунт? Это действие невозможно отменить.", color = Color.Gray) },
            confirmButton = {
                TextButton(onClick = {
                    val user = auth.currentUser
                    val userId = user?.uid
                    if (userId != null) {
                        userProfileViewModel.deleteUserAccount(userId)
                        userProfileViewModel.deletionResult.observe(lifecycleOwner) { success ->
                            val msg = if (success) {
                                "Аккаунт успешно удалён"
                            } else {
                                "Ошибка при удалении аккаунта"
                            }
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            if (success) {
                                mainNavController.navigate("registration") {
                                    popUpTo(0)
                                }
                            }
                        }
                    } else {
                        Toast.makeText(context, "Не удалось получить ID пользователя", Toast.LENGTH_SHORT).show()
                    }
                    showDeleteDialog = false
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
    ) {
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

        Text(
            text = "Информация о профиле",
            modifier = Modifier.align(Alignment.Center),
            overflow = TextOverflow.Ellipsis,
            fontFamily = Font(R.font.manrope_semibold).toFontFamily(),
            fontSize = 18.sp,
            style = LocalTextStyle.current.copy(lineHeight = 20.sp),
        )

        Box(modifier = Modifier.align(Alignment.CenterEnd)) {
            IconButton(onClick = { expanded = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_exit),
                    contentDescription = "Меню",
                    modifier = Modifier.size(20.dp)
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
                                fontFamily = Font(R.font.inter_regular).toFontFamily(),
                                color = colorResource(id = R.color.black)
                            )
                        },
                        onClick = {
                            expanded = false
                            when (action) {
                                "Выйти из аккаунта" -> {
                                    showExitDialog = true
                                }

                                "Удалить аккаунт" -> {
                                    showDeleteDialog = true
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}