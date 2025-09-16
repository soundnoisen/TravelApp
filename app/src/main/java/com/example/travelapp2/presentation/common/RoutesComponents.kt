package com.example.travelapp2.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.travelapp2.R
import com.example.travelapp2.data.models.RouteFirebase
import com.example.travelapp2.emptyPhotos
import com.example.travelapp2.presentation.viewmodels.RouteFirebaseViewModel
import com.example.travelapp2.presentation.viewmodels.UserPreferencesViewModel


@Composable
fun LazyColumnRoutes(routeList: List<RouteFirebase>, userPreferencesViewModel: UserPreferencesViewModel, navController: NavHostController, routeFirebaseViewModel: RouteFirebaseViewModel) {
    LazyColumn(
        Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()) {
        itemsIndexed(routeList) { _, route ->
            RouteCard(
                route = route,
                onClick = {
                    navController.navigate("RouteScreen/${route.id}")
                    routeFirebaseViewModel.onRouteViewed(route)
                    userPreferencesViewModel.trackAndSaveRouteView(route)
                },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .animateItem(fadeInSpec = null, fadeOutSpec = null)
            )
        }
    }
}

@Composable
fun RouteCard(route: RouteFirebase, onClick: () -> Unit, modifier: Modifier) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .height(146.dp)
            .shadow(
                16.dp,
                shape = RoundedCornerShape(10.dp),
                spotColor = Color.Black.copy(alpha = 0.06f)
            ),
        colors = CardDefaults.cardColors(Color.White),
        onClick = onClick
    ) {
        Row(Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
            ) {
                ImageRoute(route)
            }
            Box(
                Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                Column {
                    Text(
                        text = route.title,
                        modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = Font(R.font.manrope_semibold).toFontFamily(),
                        fontSize = 16.sp,
                        style = LocalTextStyle.current.copy(lineHeight = 20.sp),
                    )
                    CustomText(route.city, 14, R.font.manrope_medium, 20, Modifier.padding(top = 2.dp))
                    CustomText("Длительность: " + if (route.routeTime == "День или больше") "${route.exactDays} дня" else route.routeTime, 14, R.font.manrope_medium, 20, Modifier.padding(top = 2.dp))
                    Text(
                        text = route.description.ifEmpty { "Описание отсутствует." },
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = Font(R.font.inter_regular).toFontFamily(),
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.hint),
                        style = LocalTextStyle.current.copy(lineHeight = 20.sp),
                    )
                }
            }
        }
    }
}

@Composable
fun ImageRoute(route: RouteFirebase) {
    val imageUrl = if (route.photos.isNotEmpty()) {
        route.photos.first()
    } else {
        emptyPhotos
    }

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = null,
        modifier = Modifier
            .fillMaxHeight()
            .width(150.dp),
        contentScale = ContentScale.Crop
    )
}

