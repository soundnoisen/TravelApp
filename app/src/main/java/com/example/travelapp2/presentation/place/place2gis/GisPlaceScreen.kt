package com.example.travelapp2.presentation.place.place2gis

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.travelapp2.R
import com.example.travelapp2.data.models.PlaceFirebase
import com.example.travelapp2.presentation.place.component.LoadPlaceImage
import com.example.travelapp2.presentation.place.place2gis.component.PlaceComments
import com.example.travelapp2.presentation.place.place2gis.component.PlaceRatingAndReviews
import com.example.travelapp2.presentation.place.place2gis.component.ScheduleView
import com.example.travelapp2.presentation.profile.profile.UserProfileViewModel
import com.example.travelapp2.presentation.route.component.Header
import com.example.travelapp2.presentation.route.component.TextTab
import com.example.travelapp2.presentation.route.component.TextView
import com.example.travelapp2.presentation.viewmodels.GIS2ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun GisPlaceScreen(
    navController: NavHostController,
    gis2ViewModel: GIS2ViewModel,
    userProfileViewModel: UserProfileViewModel
) {

    val context = LocalContext.current

    val place by gis2ViewModel.place.collectAsState()

    var isSaved by remember { mutableStateOf(false) }

    LaunchedEffect(place) {
        place?.let {
            userProfileViewModel.toggleNowSavedPlace(it) { isNowSaved ->
                isSaved = isNowSaved
            }
        }
    }


    if (place != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.background))
        ) {

            Box(modifier = Modifier.fillMaxWidth()) {
                LoadPlaceImage(place!!.mainPhotoUrl)
                Header(
                    navController,
                    if (place!!.mainPhotoUrl.isEmpty()) Color.Black else Color.White,
                    isSaved = isSaved,
                    onSave = {
                        isSaved = !isSaved
                        userProfileViewModel.toggleSavedPlace(place!!) { isNowSaved ->
                            val msg =
                                if (isNowSaved) "Место добавлено в избранное" else "Место удалено"
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                    },
                    showSaveButton = true
                )
            }


            Column(Modifier.padding(top = 400.dp)) {
                Column(Modifier.padding(start = 16.dp, bottom = 10.dp)) {
                    place?.name?.let { TextView(it, 20,  FontWeight.SemiBold, Color.White) }
                    place?.address?.let {
                        TextView(
                            it,
                            16,
                            FontWeight.Normal,
                            Color.White
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .background(colorResource(id = R.color.background))
                ) {
                    Column {
                        PlaceRatingAndReviews(place!!)

                        val tabs = listOf("Расписание", "Комментарии")
                        var selectedTabIndex by remember { mutableIntStateOf(0) }

                        TabRow(
                            selectedTabIndex = selectedTabIndex,
                            contentColor = colorResource(id = R.color.main),
                            indicator = { tabPositions ->
                                SecondaryIndicator(
                                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                    color = colorResource(id = R.color.main)
                                )
                            },
                            divider = {
                                Divider(
                                    color = colorResource(id = R.color.hint),
                                    thickness = 1.dp
                                )
                            }
                        ) {
                            tabs.forEachIndexed { index, title ->
                                Tab(
                                    selected = selectedTabIndex == index,
                                    onClick = { selectedTabIndex = index },
                                ) {
                                    TextTab(
                                        title,
                                        16,
                                        FontWeight.Normal,                                        20,
                                        if (selectedTabIndex == index) colorResource(id = R.color.main) else colorResource(
                                            id = R.color.hint
                                        ),
                                        dp = 0,
                                        )
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .padding(16.dp)
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                        ) {
                            when (selectedTabIndex) {
                                0 -> ScheduleView(place!!)
                                1 -> PlaceComments(place!!)
                            }
                        }
                    }
                }
            }
        }
    }
}
