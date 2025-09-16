package com.example.travelapp2.presentation.route.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.travelapp2.R
import com.example.travelapp2.emptyContentPhoto


@Composable
fun ImageCarousel(modifier: Modifier = Modifier, photos: List<String>?, indicatorTopPadding: Dp = 60.dp) {
    val photoList = if (photos.isNullOrEmpty()) {
        listOf(emptyContentPhoto)
    } else {
        photos
    }

    val pagerState = rememberPagerState { photoList.size }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = modifier.fillMaxWidth()
        ) { page ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photoList[page])
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop
            )
        }

        if (photoList.isNotEmpty()) {
            Box(modifier = Modifier.padding(top = indicatorTopPadding)) {
                DotsIndicator(pagerState)
            }
        }
    }
}



@Composable
fun DotsIndicator(pagerState: PagerState) {
    val pageCount = pagerState.pageCount
    val currentPage = pagerState.currentPage

    if (pageCount > 1) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val maxDots = 5

            val startPage = if (pageCount <= maxDots) {
                0
            } else {
                (currentPage - maxDots / 2).coerceIn(0, pageCount - maxDots)
            }

            val endPage = if (pageCount <= maxDots) {
                pageCount
            } else {
                (startPage + maxDots).coerceAtMost(pageCount)
            }

            for (i in startPage until endPage) {
                val isCurrent = i == currentPage
                val size = if (isCurrent) 8.dp else 6.dp
                val color = if (isCurrent) Color.White else colorResource(id = R.color.hint)

                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(size)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}