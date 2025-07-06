package com.example.toolsfordriver.data.model

import java.time.YearMonth

data class TripDateCategory(
    val name: String,
    val yearMonth: YearMonth,
    val items: List<Trip>
)