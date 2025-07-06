package com.example.toolsfordriver.data.model

import java.time.YearMonth

data class FreightDateCategory(
    val name: String,
    val yearMonth: YearMonth,
    val items: List<Freight>
)