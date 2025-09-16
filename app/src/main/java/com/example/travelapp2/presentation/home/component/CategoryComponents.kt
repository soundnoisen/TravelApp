package com.example.travelapp2.presentation.home.component


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelapp2.R
import com.example.travelapp2.presentation.viewmodels.UserRecommendationViewModel
import com.example.travelapp2.presentation.home.models.Category
import com.example.travelapp2.presentation.viewmodels.UserPreferencesViewModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyRowCategories(
    categoryList: List<Category>,
    selectedCategoryIndex: Int,
    userPreferencesViewModel: UserPreferencesViewModel,
    modifier: Modifier = Modifier,
    onCategorySelected: (Int, String) -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(categoryList) { index, category ->
            CategoryCard(
                category = category,
                isSelected = selectedCategoryIndex == index,
                onClick = {
                    onCategorySelected(index, category.categoryGeoApify)
                    userPreferencesViewModel.updatePlaceCategoryPreferenceOnView(category.categoryGeoApify)
                          },
                modifier = if (index == 1 ) modifier else Modifier
            )
        }
    }
}


@Composable
fun CategoryCard(category: Category, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier) {
    val (backgroundColor, borderColor) = animateCategoryColors(isSelected)

    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .height(56.dp)
            .drawBehind {
                drawRoundRect(
                    color = borderColor,
                    size = size.copy(width = size.width - 2.dp.toPx(), height = size.height - 2.dp.toPx()),
                    cornerRadius = CornerRadius(10.dp.toPx())
                )
            }
            .shadow(
                elevation = if (isSelected) 16.dp else 0.dp,
                shape = RoundedCornerShape(10.dp),
                spotColor = Color.Black.copy(alpha = 0.06f)
            ),
        colors = CardDefaults.cardColors(backgroundColor),
        onClick = onClick
    ) {
        Row(Modifier.fillMaxHeight()) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        if (isSelected) colorResource(id = R.color.background)
                        else colorResource(id = R.color.unselected_card_image)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = category.imageResId),
                    contentDescription = null,
                    modifier = Modifier.size(42.dp)
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp)
                    .wrapContentHeight(Alignment.CenterVertically),
                text = category.name,
                fontSize = 16.sp,
                fontFamily = Font(R.font.manrope_semibold).toFontFamily(),
                style = LocalTextStyle.current.copy(lineHeight = 20.sp),
            )
        }
    }
}


@Composable
fun animateCategoryColors(isSelected: Boolean): Pair<Color, Color> {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else colorResource(id = R.color.unselected_card),
        animationSpec = tween(durationMillis = 500), label = ""
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) Color.Transparent else colorResource(id = R.color.unselected_card_image),
        animationSpec = tween(durationMillis = 500), label = ""
    )

    return backgroundColor to borderColor
}
