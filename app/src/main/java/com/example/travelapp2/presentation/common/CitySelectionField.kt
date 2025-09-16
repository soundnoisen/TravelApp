package com.example.travelapp2.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.travelapp2.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySelectionField(
    selectedCity: String,
    onCitySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val cities = listOf(
        "Казань", "Набережные Челны", "Нижнекамск", "Альметьевск", "Зеленодольск",
        "Бугульма", "Елабуга", "Лениногорск", "Чистополь", "Заинск",
        "Азнакаево", "Нурлат", "Бавлы", "Менделеевск", "Буинск",
        "Агрыз", "Арск", "Кукмор", "Мензелинск", "Мамадыш",
        "Тетюши", "Актаныш", "Камские Поляны", "Болгар", "Лаишево",
        "Рыбная Слобода", "Алексеевское", "Джалиль", "Уруссу", "Апастово",
        "Высокая Гора", "Карабаш", "Камское Устье", "Кукмор", "Муслюмово",
        "Новый Кырлай", "Пестрецы", "Сарманово", "Тюлячи", "Черемшан"
    )

    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var showCityNotFound by remember { mutableStateOf(false) }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(
                    16.dp,
                    shape = RoundedCornerShape(10.dp),
                    spotColor = Color.Black.copy(alpha = 0.06f)
                )
                .background(Color.White, shape = RoundedCornerShape(10.dp))
        ) {
            TextField(
                value = selectedCity,
                onValueChange = { input ->
                    onCitySelected(input)
                    expanded = input.isNotEmpty()
                    showCityNotFound =
                        input.isNotEmpty() && cities.none { it.contains(input, ignoreCase = true) }
                },
                placeholder = {
                    androidx.compose.material.Text(
                        text = "Введите город",
                        color = colorResource(id = R.color.hint),
                        fontSize = 16.sp,
                        fontFamily = Font(R.font.inter_regular).toFontFamily()
                    )
                },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontFamily = Font(R.font.inter_regular).toFontFamily()
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        textFieldSize = coordinates.size.toSize()
                    },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        expanded = false
                        showCityNotFound = selectedCity.isNotEmpty() && cities.none {
                            it.equals(
                                selectedCity,
                                ignoreCase = true
                            )
                        }
                    }
                )
            )
        }

        val filteredCities = cities.filter { it.contains(selectedCity, ignoreCase = true) }

        if (expanded && filteredCities.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 160.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                    .border(1.dp, colorResource(id = R.color.unselected_card_image), RoundedCornerShape(10.dp))
                    .padding(vertical = 4.dp)
            ) {
                items(filteredCities) { city ->
                    Text(
                        text = city,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onCitySelected(city)
                                expanded = false
                                showCityNotFound = false
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        fontSize = 16.sp,
                        fontFamily = Font(R.font.inter_regular).toFontFamily()
                    )
                }
            }
        }

        if (showCityNotFound) {
            Text(
                text = "Город не найден",
                color = Color.Red,
                fontFamily = Font(R.font.inter_regular).toFontFamily(),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}