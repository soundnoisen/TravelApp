package com.example.travelapp2.presentation.profile.profile.component

import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.travelapp2.R
import com.example.travelapp2.presentation.route.component.TextTab


@Composable
fun ProfileTabRow(
    tabs: List<String>,
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    modifier2: Modifier = Modifier,
    onTabSelected: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        contentColor = colorResource(id = R.color.main),
        indicator = { tabPositions ->
            SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
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
                onClick = { onTabSelected(index) },
                modifier = if (index == 0) modifier else modifier2
                    .indication(interactionSource = remember { MutableInteractionSource() }, indication = null)
            ) {
                val textColor = if (selectedTabIndex == index) colorResource(id = R.color.main) else colorResource(id = R.color.hint)
                TextTab(
                    text = title,
                    size = 16,
                    font =  FontWeight.Normal,
                    lineHeight = 20,
                    color = textColor,
                )
            }
        }
    }
}
