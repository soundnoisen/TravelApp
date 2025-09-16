package com.example.travelapp2.presentation.tour.component

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
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
import androidx.navigation.NavHostController
import com.example.travelapp2.R
import com.example.travelapp2.data.models.TourFirebase
import com.example.travelapp2.presentation.tourCreate.TourCreationViewModel
import com.example.travelapp2.presentation.viewmodels.ToursViewModel


@Composable
fun HeaderTourAdmin(
    title: String,
    navController: NavHostController,
    toursViewModel: ToursViewModel,
    tourId: String,
) {
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            shape = RoundedCornerShape(10.dp),
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(text ="Удалить поездку?", fontSize = 18.sp, color = Color.Black) },
            text = { Text("Вы уверены, что хотите удалить эту поездку? Это действие невозможно отменить.", color = Color.Gray) },
            confirmButton = {
                TextButton(onClick = {
                    toursViewModel.deleteTour(tourId)
                    showDeleteDialog = false
                    navController.popBackStack()
                    navController.popBackStack()
                    Toast.makeText(context, "Поездка удалёна", Toast.LENGTH_SHORT).show()
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
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            overflow = TextOverflow.Ellipsis,
            fontFamily = Font(R.font.manrope_semibold).toFontFamily(),
            fontSize = 18.sp,
            color = Color.Black,
            style = LocalTextStyle.current.copy(lineHeight = 20.sp),
            modifier = Modifier.align(Alignment.Center)
        )

        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back3),
                tint = Color.Black,
                contentDescription = null
            )
        }

        Box(modifier = Modifier.align(Alignment.CenterEnd)) {
            IconButton(
                onClick = { showDeleteDialog = true },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete_point),
                    modifier = Modifier.size(16.dp),
                    tint = Color.Black,
                    contentDescription = null
                )
            }
        }
    }

}

