package com.example.toolsfordriver.data.model

data class Category(
    val name: String,
    val yearMonth: String,
    val items: List<Trip>
)