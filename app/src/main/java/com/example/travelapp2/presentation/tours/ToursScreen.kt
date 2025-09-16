package com.example.travelapp2.presentation.tours

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.travelapp2.R
import com.example.travelapp2.presentation.route.component.TextTab
import com.example.travelapp2.presentation.tours.component.HeaderTours
import com.example.travelapp2.presentation.tours.component.TourCard
import com.example.travelapp2.presentation.tours.component.TourParticipantsNumber
import com.example.travelapp2.presentation.viewmodels.ToursViewModel

@Composable
fun ToursScreen(navController: NavHostController, toursViewModel: ToursViewModel) {
    val publicTours by toursViewModel.publicTours.observeAsState(emptyList())
    val userTours by toursViewModel.userTours.observeAsState(emptyList())

    var selectedTabIndex by remember { mutableIntStateOf(0) }


    Column(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(id = R.color.background))) {

        Spacer(modifier = Modifier.height(40.dp))
        HeaderTours(title = "Поездки",
            endIcon = painterResource(id = R.drawable.ic_add_point),
            endIconAction = {navController.navigate("TourCreation")},
            tint = Color.Black
            )


        val tabs = listOf("Публичные поездки", "Мои поездки")

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
                    onClick = { selectedTabIndex = index }
                ) {
                    val color =
                        if (selectedTabIndex == index) colorResource(id = R.color.main) else colorResource(
                            id = R.color.hint
                        )
                    TextTab(title, 16, FontWeight.Normal, 20, color)
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                when (selectedTabIndex) {

                    0 -> items(publicTours) { tour ->
                        Box(modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(vertical = 8.dp))
                        {
                            TourCard(tour) {
                                navController.navigate("TourScreen/${tour.id}")
                            }
                        }
                    }

                    1 -> items(userTours) { tour ->
                        Box(modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(vertical = 8.dp))
                        {
                            TourCard(tour) {
                                navController.navigate("TourScreen/${tour.id}")
                            }
                        }
                    }
                }
            }
        }
    }
}


