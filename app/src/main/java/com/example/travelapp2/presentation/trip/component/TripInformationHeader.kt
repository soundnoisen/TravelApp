package com.example.travelapp2.presentation.trip.component

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.travelapp2.R
import com.example.travelapp2.data.models.LocationPointFirebase
import com.example.travelapp2.data.models.TripFirebase
import com.example.travelapp2.presentation.profile.createTrip.TripCreationViewModel
import com.example.travelapp2.presentation.viewmodels.LocationPointFirebaseViewModel
import com.example.travelapp2.presentation.viewmodels.TripFirebaseViewModel

@Composable
fun TripInformationHeader(
    navController: NavHostController,
    tripFirebaseViewModel: TripFirebaseViewModel,
    trip: TripFirebase,
    tripId: String,
    selectedIndex: Int,
    isMyTrip: Boolean,
    tripCreationViewModel: TripCreationViewModel,
    locations: List<LocationPointFirebase>,
    locationPointFirebaseViewModel: LocationPointFirebaseViewModel
) {
    val context = LocalContext.current

    val color = if (trip.photos.isNotEmpty() && selectedIndex == 0) Color.White else Color.Black

    val actions = listOf("Изменить путешествие", "Удалить путешествие")
    var expanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            shape = RoundedCornerShape(10.dp),
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(text ="Удалить путешествие?", fontSize = 18.sp, color = Color.Black) },
            text = { Text("Вы уверены, что хотите удалить это путешествие? Это действие невозможно отменить.", color = Color.Gray) },
            confirmButton = {
                TextButton(onClick = {
                    tripFirebaseViewModel.deleteTrip(tripId)
                    showDeleteDialog = false
                    navController.popBackStack()
                    Toast.makeText(context, "Путешествие удалёно", Toast.LENGTH_SHORT).show()
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

        if (isMyTrip) {
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
                                    fontFamily = Font(R.font.inter_regular).toFontFamily(),
                                    color = colorResource(id = R.color.black)
                                )
                            },
                            onClick = {
                                expanded = false
                                when (action) {
                                    "Изменить путешествие" -> {
                                        tripCreationViewModel.setTrip(trip)
                                        locationPointFirebaseViewModel.setLocations(locations)
                                        navController.navigate("TripDetails")
                                    }

                                    "Удалить путешествие" -> {
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
}
