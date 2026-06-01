package com.hectoralmor.mangos.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hectoralmor.mangos.R

val Josefin = FontFamily(
    Font(R.font.josefinsans_regular, FontWeight.Normal),
    Font(R.font.josefinsans_medium, FontWeight.Medium),
    Font(R.font.josefinsans_bold, FontWeight.Bold),
    Font(R.font.josefinsans_extrabold, FontWeight.ExtraBold)
)

val Typography = Typography(

    bodyLarge = TextStyle(
        fontFamily = Josefin,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = Josefin,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),

    titleLarge = TextStyle(
        fontFamily = Josefin,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),

    headlineLarge = TextStyle(
        fontFamily = Josefin,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 28.sp
    ),

    labelLarge = TextStyle(
        fontFamily = Josefin,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),

    labelMedium = TextStyle(
        fontFamily = Josefin,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    ),

    labelSmall = TextStyle(
        fontFamily = Josefin,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp
    )
)