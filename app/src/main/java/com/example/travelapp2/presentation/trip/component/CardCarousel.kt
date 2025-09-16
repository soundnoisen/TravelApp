package com.example.travelapp2.presentation.trip.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.travelapp2.R
import com.example.travelapp2.data.models.LocationPointFirebase
import kotlinx.coroutines.launch


@Composable
fun CardCarousel(
    locations: List<LocationPointFirebase>,
    selectedIndex: Int,
    listState: LazyListState,
    onSelectedIndexChanged: (Int) -> Unit,
    onCardClick: (LocationPointFirebase) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val countPoint = locations.size
    val currentIndex by remember {
        derivedStateOf {
            val firstItemIndex = listState.firstVisibleItemIndex
            val firstItemOffset = listState.firstVisibleItemScrollOffset
            if (firstItemOffset > 0) firstItemIndex + 1 else firstItemIndex
        }
    }

    LaunchedEffect(currentIndex) {
        onSelectedIndexChanged(currentIndex)
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyRow(
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            items(countPoint) { index ->

                val location = locations[index]

                PointCard(
                    isActive = index == selectedIndex,
                    name = location.title,
                    city = location.city,
                    image = location.photos.firstOrNull(),
                    onClick = {
                        onSelectedIndexChanged(index)
                        onCardClick(location)
                        coroutineScope.launch {
                            listState.animateScrollToItem(index)
                        }
                    }
                )

                if (index != (countPoint - 1)) {
                    Column {
                        Spacer(modifier = Modifier.padding(top = 86.dp))
                        Box(
                            modifier = Modifier
                                .width(16.dp)
                                .height(1.dp)
                                .background(colorResource(id = R.color.hint))
                        )
                    }
                }
            }
        }
    }
}
