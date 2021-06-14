package com.example.covid19.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val Green = Color(0xFF15bd0f)
val Black = Color(0xFF000000)
val DarkGrey = Color(0xFF1c1b1a)
val MilkWhite = Color(0xFFECF0F1)
val White = Color(0xFFFFFFFF)
val Red = Color(0xFFff2200)
val Orange = Color(0xFFff9100)
val Blue = Color(0xFF0088ff)
val LightGreen = Color(0xFF82d999)
val LightRed = Color(0xFFeb877a)
val LightOrange = Color(0xFFf0b960)
val DeepBlue = Color(0xFF272191)

val LightColorPalette = lightColors(
    primary = DeepBlue,
    primaryVariant = Blue,
    secondary = Black,
    secondaryVariant = DarkGrey,
    background = MilkWhite,
    surface = White,
    error = Red
)

val DarkColorPalette = darkColors(
    primary = DeepBlue,
    primaryVariant = Blue,
    secondary = White,
    secondaryVariant = MilkWhite,
    background = DarkGrey,
    surface = Black,
    error = Red
)
