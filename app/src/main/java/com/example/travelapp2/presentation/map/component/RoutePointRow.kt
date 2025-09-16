package com.example.travelapp2.presentation.map.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelapp2.R
import com.example.travelapp2.presentation.viewmodels.MapHolderViewModel
import org.osmdroid.util.GeoPoint


@Composable
fun RoutePointRow(
    index: Int,
    onTextChange: (String) -> Unit,
    onDelete: () -> Unit,
    onMapClick: (GeoPoint, String?) -> Unit,
    point: String,
) {
    var showLocationPicker by remember { mutableStateOf(false) }

    if (showLocationPicker) {
        LocationPickerDialog(
            onLocationSelected = { geoPoint, name, address, city ->
                onMapClick(geoPoint, if (name.isNullOrEmpty()) address else name)
                showLocationPicker = false
            },
            onDismiss = { showLocationPicker = false },
        )
    }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .weight(1f)
                    .padding(end = 8.dp)) {
                if (point.isEmpty()) {
                    Text(
                        text = "${index+1}. Куда",
                        fontSize = 16.sp,
                        fontFamily = Font(R.font.inter_regular).toFontFamily(),
                        color = colorResource(id = R.color.hint),
                        style = LocalTextStyle.current.copy(lineHeight = 20.sp)
                    )
                }

                BasicTextField(
                    value = "${index+1}. $point",
                    onValueChange = onTextChange,
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontFamily = Font(R.font.inter_regular).toFontFamily(),
                        lineHeight = 20.sp
                    ),
                    singleLine = true,
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                )
            }
        }

        Row(modifier = Modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(modifier = Modifier.size(40.dp), onClick = { showLocationPicker = true }) {
                Icon(
                    modifier = Modifier.size(26.dp),
                    painter = painterResource(id = R.drawable.ic_point_geo_location),
                    contentDescription = null,
                    tint = colorResource(id = R.color.main)
                )
            }

            IconButton(modifier = Modifier.size(40.dp), onClick = onDelete) {
                Icon(
                    modifier = Modifier.size(14.dp),
                    painter = painterResource(id = R.drawable.ic_delete_point),
                    contentDescription = null,
                    tint = colorResource(id = R.color.hint)
                )
            }
        }
    }
}