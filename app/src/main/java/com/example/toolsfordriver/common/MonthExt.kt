package com.example.toolsfordriver.common

import com.example.toolsfordriver.R
import java.time.Month

fun Month.getNameStrRes(): Int? {
    val monthNameMap = mapOf(
        Month.JANUARY to R.string.january,
        Month.FEBRUARY to R.string.february,
        Month.MARCH to R.string.march,
        Month.APRIL to R.string.april,
        Month.MAY to R.string.may,
        Month.JUNE to R.string.june,
        Month.JULY to R.string.july,
        Month.AUGUST to R.string.august,
        Month.SEPTEMBER to R.string.september,
        Month.OCTOBER to R.string.october,
        Month.NOVEMBER to R.string.november,
        Month.DECEMBER to R.string.december
    )

    return monthNameMap.filter { it.key == this }.map { it.value }.firstOrNull()
}