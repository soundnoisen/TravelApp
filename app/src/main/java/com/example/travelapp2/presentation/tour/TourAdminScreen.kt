package com.example.travelapp2.presentation.tour

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.travelapp2.R
import com.example.travelapp2.data.models.TourFirebase
import com.example.travelapp2.data.models.UserProfileFirebase
import com.example.travelapp2.domain.validation.isFirstDateEarlier
import com.example.travelapp2.domain.validation.isValidDate
import com.example.travelapp2.presentation.common.CitySelectionField
import com.example.travelapp2.presentation.common.CustomButton
import com.example.travelapp2.presentation.common.CustomText
import com.example.travelapp2.presentation.common.DateRangePicker
import com.example.travelapp2.presentation.profile.component.AddPhotoButton
import com.example.travelapp2.presentation.profile.component.CustomField
import com.example.travelapp2.presentation.profile.component.ImageThumbnail
import com.example.travelapp2.presentation.profile.component.OpenImageDialog
import com.example.travelapp2.presentation.profile.createRoute.component.showToast
import com.example.travelapp2.presentation.tour.component.HeaderTourAdmin
import com.example.travelapp2.presentation.tour.component.TextFieldNumber
import com.example.travelapp2.presentation.tour.component.UserRow
import com.example.travelapp2.presentation.tourCreate.TourCreationViewModel
import com.example.travelapp2.presentation.util.uploadToCloudinary
import com.example.travelapp2.presentation.util.uriToFile
import com.example.travelapp2.presentation.viewmodels.ToursViewModel
import kotlinx.coroutines.launch

@Composable
fun TourAdminScreen(
    navController: NavHostController,
    tourId: String,
    toursViewModel: ToursViewModel,
    tourCreationViewModel: TourCreationViewModel
) {
    val context = LocalContext.current


    LaunchedEffect(tourId) {
        toursViewModel.listenToTourById(tourId)
        toursViewModel.listenToParticipants(tourId)
    }

    val tour by toursViewModel.foundTour.observeAsState()
    var usersRequesting by remember { mutableStateOf<List<UserProfileFirebase>>(emptyList()) }
    var participants by remember { mutableStateOf<List<UserProfileFirebase>>(emptyList()) }

    var tourName by remember { mutableStateOf("") }
    var tourDescription by remember { mutableStateOf("") }
    var tourCity by remember { mutableStateOf("") }
    var tourStartDate by remember { mutableStateOf("") }
    var tourEndDate by remember { mutableStateOf("") }
    var isPublic by remember { mutableStateOf(false) }
    var maxParticipants by remember { mutableStateOf("") }
    var photoUrl by remember { mutableStateOf("") }
    var selectedImage by remember { mutableStateOf<Uri?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var openImage by remember { mutableStateOf<Uri?>(null) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showCancelDialog by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedImage = uri
    }

    LaunchedEffect(tour) {
        tour?.let {
            tourName = it.title
            tourDescription = it.description
            tourCity = it.city
            tourStartDate = it.startDate
            tourEndDate = it.endDate
            isPublic = it.public
            maxParticipants = it.maxParticipants.toString()
            photoUrl = it.photoUrl
        }
    }

    LaunchedEffect(tour?.requests) {
        tour?.requests?.let { userIds ->
            toursViewModel.loadUsersByIds(userIds) {
                usersRequesting = it
            }
        }
    }

    LaunchedEffect(tour?.participants) {
        tour?.participants?.let { userIds ->
            toursViewModel.loadUsersByIds(userIds) {
                participants = it
            }
        }
    }

    val scope = rememberCoroutineScope()


    fun onSavePressed() {
        scope.launch {
            when {
                tourName.isEmpty() -> showToast(context, "Напишите название поездки")
                tourStartDate.isEmpty() || tourEndDate.isEmpty() -> showToast(context, "Введите дату поездки")
                tourCity.isEmpty() -> showToast(context, "Укажите город")
                else -> {
                    val validStart = isValidDate(tourStartDate)
                    val validEnd   = isValidDate(tourEndDate)
                    val logicalOk  = isFirstDateEarlier(tourStartDate, tourEndDate)

                    if (!validStart || !validEnd) {
                        showToast(context, "Некорректный формат даты.")
                        return@launch
                    }
                    if (!logicalOk) {
                        showToast(context, "Дата начала не может быть позже даты окончания")
                        return@launch
                    }
                    selectedImage?.let { uri ->
                        val file = uriToFile(uri, context)
                        file?.let {
                            val uploaded = uploadToCloudinary(it)
                            if (uploaded != null) {
                                photoUrl = uploaded
                            } else {
                                showToast(context, "Не удалось загрузить фото")
                            }
                        }
                    }

                    val updatedTour = TourFirebase(
                        title = tourName,
                        startDate = tourStartDate,
                        endDate   = tourEndDate,
                        city      = tourCity,
                        description    = tourDescription,
                        public         = isPublic,
                        maxParticipants = maxParticipants.toIntOrNull() ?: 1,
                        photoUrl       = photoUrl
                    )

                    toursViewModel.updateTour(tourId, updatedTour)
                    navController.popBackStack()
                }
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp)
    ) {
        HeaderTourAdmin(
            title = "Управление поездкой",
            navController,
            toursViewModel,
            tourId,
        )

        LazyColumn(Modifier.padding(horizontal = 16.dp)) {

            if (usersRequesting.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomText("Заявки на вступление", 18, R.font.manrope_semibold, 26)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(usersRequesting) { user ->

                    if (showCancelDialog) {
                        AlertDialog(
                            shape = RoundedCornerShape(10.dp),
                            onDismissRequest = { showCancelDialog = false },
                            title = { Text(text ="Отклонить запрос?", fontSize = 18.sp, color = Color.Black) },
                            text = { Text("Вы уверены, что хотите отклонить запрос этого пользователя? Это действие невозможно отменить.", color = Color.Gray) },
                            confirmButton = {
                                TextButton(onClick = {
                                    toursViewModel.rejectJoinRequest(tourId, user.userId)
                                    showCancelDialog = false
                                    Toast.makeText(context, "Запрос отклонен", Toast.LENGTH_SHORT).show()
                                }) {
                                    Text("Отклонить", color = Color.Red)
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showCancelDialog = false }) {
                                    Text("Отмена", color = Color.Gray)
                                }
                            },
                            containerColor = colorResource(id = R.color.background)
                        )
                    }


                    UserRow(
                        user = user,
                        isAdmin = user.userId == tour!!.adminId,
                        navController = navController,
                        onClickTrailingIcon = {
                            IconButton(onClick = {
                                toursViewModel.approveJoinRequest(tourId, user.userId)
                            }) {
                                Icon(Icons.Default.Check, contentDescription = "Принять")
                            }
                            IconButton(onClick = {
                                showCancelDialog = true
                            }) {
                                Icon(Icons.Default.Close, contentDescription = "Отклонить")
                            }
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                CustomText("Участники", 18, R.font.manrope_semibold, 26)
            }

            items(participants) { user ->

                if (showDeleteDialog) {
                    AlertDialog(
                        shape = RoundedCornerShape(10.dp),
                        onDismissRequest = { showDeleteDialog = false },
                        title = { Text(text ="Удалить участника?", fontSize = 18.sp, color = Color.Black) },
                        text = { Text("Вы уверены, что хотите удалить этого участника? Это действие невозможно отменить.", color = Color.Gray) },
                        confirmButton = {
                            TextButton(onClick = {
                                toursViewModel.removeParticipant(tourId, user.userId)
                                showDeleteDialog = false
                                Toast.makeText(context, "Участник удален", Toast.LENGTH_SHORT).show()
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

                val isAdmin = user.userId == tour!!.adminId
                UserRow(
                    user = user.copy(
                        displayName = if (isAdmin) "Вы" else user.displayName
                    ),
                    isAdmin = isAdmin,
                    navController = navController,
                    onClickTrailingIcon = if (!isAdmin) {
                        {
                            IconButton(onClick = {
                                showDeleteDialog = true
                            }) {
                                Icon(Icons.Default.Clear, contentDescription = "Удалить")
                            }
                        }
                    } else null
                )

            }


            item {
                Spacer(modifier = Modifier.height(8.dp))
                CustomText("Местоположение", 18, R.font.manrope_semibold, 26)
                Spacer(Modifier.height(8.dp))
                CitySelectionField(
                    selectedCity = tourCity,
                    onCitySelected = { city -> tourCity = city }
                )

                Spacer(modifier = Modifier.height(16.dp))
                CustomText("Название поездки", 18, R.font.manrope_semibold, 26)
                Spacer(Modifier.height(8.dp))
                CustomField(
                    text = tourName,
                    placeholder = "Введите название поездки",
                    onTextChanged = { input -> tourName = input },
                )

                Spacer(modifier = Modifier.height(16.dp))
                CustomText("Дата поездки", 18, R.font.manrope_semibold, 26)
                Spacer(Modifier.height(8.dp))
                DateRangePicker(
                    startDate = tourStartDate,
                    endDate = tourEndDate,
                    onStartDateChanged = { tourStartDate = it },
                    onEndDateChanged = { tourEndDate = it },
                    errorMessage = errorMessage
                )

                Spacer(Modifier.height(16.dp))
                CustomText("Описание поездки", 18, R.font.manrope_semibold, 26)
                Spacer(Modifier.height(8.dp))
                CustomField(
                    text = tourDescription,
                    placeholder = "Опишите цель поездки",
                    onTextChanged = { input -> tourDescription = input },
                )

                Spacer(Modifier.height(16.dp))
                CustomText("Максимальное кол-во участников", 18, R.font.manrope_semibold, 26)
                Spacer(Modifier.height(8.dp))
                TextFieldNumber(
                    value = maxParticipants,
                    onValueChange = { newValue ->
                        if ((newValue.all { it.isDigit() } && newValue.toIntOrNull()?.let { it > 1 } == true) || newValue.isEmpty()) {
                            maxParticipants = newValue
                        }
                    }
                )



                Spacer(Modifier.height(16.dp))
                CustomText("Фотография маршрута", 18, R.font.manrope_semibold, 26)
                Spacer(Modifier.height(8.dp))


                if (photoUrl.isNotEmpty() || selectedImage != null) {
                    LazyRow(modifier = Modifier.padding(bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        item {
                            if (selectedImage != null) {
                                ImageThumbnail(uri = selectedImage!!) {
                                    openImage = it
                                }
                            } else {
                                ImageThumbnail(uri = Uri.parse(photoUrl)) {
                                    openImage = it
                                }
                            }
                            AddPhotoButton { launcher.launch("image/*") }
                        }
                    }
                } else {
                    AddPhotoButton { launcher.launch("image/*") }
                }



                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomText("Сделать поездку общедоступной", 18, R.font.manrope_semibold, 26)
                    Switch(
                        checked = isPublic,
                        onCheckedChange = { newState -> isPublic = newState },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            uncheckedThumbColor = Color.White,
                            checkedTrackColor = colorResource(id = R.color.main),
                            uncheckedTrackColor = colorResource(id = R.color.switch_off),
                            uncheckedBorderColor = Color.Transparent
                        ),
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                CustomButton(onClick = { onSavePressed() }
                    , buttonText = "Сохранить")
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        openImage?.let { uri ->
            OpenImageDialog(
                uri = uri,
                onDismiss = { openImage = null },
                onDelete = {
                    selectedImage = null
                    photoUrl = ""
                    openImage = null
                }
            )
        }
    }
}
