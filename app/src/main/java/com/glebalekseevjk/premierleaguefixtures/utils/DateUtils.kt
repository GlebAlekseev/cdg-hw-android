package com.glebalekseevjk.premierleaguefixtures.utils

import java.text.SimpleDateFormat

fun parseAndGetDate(date: String, inputPattern: String, outputPattern: String): String {
    val parseFormat = SimpleDateFormat(inputPattern)
    val parseDate = parseFormat.parse(date)
    val formatter = SimpleDateFormat(outputPattern)
    return formatter.format(parseDate)
}