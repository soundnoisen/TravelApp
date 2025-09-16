package com.example.travelapp2.presentation.recommendation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelapp2.R

@Composable
fun RouteView(
    route: List<Map<String, Any>>
) {

    Column {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .padding(horizontal = 16.dp)
        ) {
            itemsIndexed(route) { index, point ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(28.dp)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(
                                    2.dp,
                                    colorResource(id = R.color.main),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                fontSize = 14.sp,
                                color = colorResource(id = R.color.main),
                                fontFamily = Font(R.font.inter_bold).toFontFamily()
                            )
                        }

                        if (index != route.lastIndex) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(56.dp)
                                    .align(Alignment.BottomCenter)
                                    .offset(y = 40.dp)
                                    .background(colorResource(id = R.color.main))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .shadow(
                                16.dp,
                                shape = RoundedCornerShape(10.dp),
                                spotColor = Color.Black.copy(alpha = 0.06f)
                            )
                            .background(Color.White, shape = RoundedCornerShape(10.dp)),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = point["name"] as? String ?: "Без названия",
                                modifier = Modifier
                                    .weight(1f),
                                fontSize = 16.sp,
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontFamily = Font(R.font.inter_regular).toFontFamily()
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

    }

}