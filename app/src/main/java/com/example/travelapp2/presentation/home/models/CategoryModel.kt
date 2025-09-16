package com.example.travelapp2.presentation.home.models

import com.example.travelapp2.R

data class Category(
    val imageResId: Int,
    val name: String,
    val categoryGeoApify: String,
)

fun categoryList(): List<Category> {
    return listOf(
        Category(R.drawable.ic_category_museum, "Музеи", "entertainment.museum"),
        Category(R.drawable.ic_category_park, "Парки", "leisure.park"),
        Category(R.drawable.ic_category_restaurant, "Рестораны", "catering.restaurant"),
        Category(R.drawable.ic_category_restaurant, "Кафе", "catering.cafe"),
        Category(R.drawable.ic_category_hotel, "Отели", "accommodation"),
        Category(R.drawable.ic_category_atm, "Банки", "service.financial"),
        Category(R.drawable.ic_category_supermarket, "Магазины", "commercial.supermarket"),
        Category(R.drawable.ic_category_pharmacy, "Аптеки", "healthcare.pharmacy"),
    )
}
