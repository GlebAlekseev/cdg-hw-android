package com.glebalekseevjk.premierleaguefixtures.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun parseAndGetDate(date: String, inputPatter: String, outputPatter: String): String{
    val parseFormatter = DateTimeFormatter.ofPattern(inputPatter)
    val localDateTime = LocalDateTime.parse(date, parseFormatter)
    val formatter = DateTimeFormatter.ofPattern(outputPatter)
    return localDateTime.format(formatter)
}

