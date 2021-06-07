package com.example.covid19.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun convertToNormalDate(str: String): String {
    return if (str.isNotEmpty()) {
        val inputFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:SS'Z'")
        val outputFormatter = DateTimeFormatter.ofPattern("MMM d")
        val date = LocalDate.parse(str, inputFormatter)
        outputFormatter.format(date)
    } else ""
}