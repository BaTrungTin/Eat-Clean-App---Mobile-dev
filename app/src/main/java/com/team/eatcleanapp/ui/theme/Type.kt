package com.team.eatcleanapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.team.eatcleanapp.R

// Set of Material typography styles to start with

val ProtestStrike = FontFamily(Font(R.font.protest_strike))
val Arima = FontFamily(
    Font(R.font.arima_regular, FontWeight.Normal),
    Font(R.font.arima_bold, FontWeight.Bold),
    Font(R.font.arima_medium, FontWeight.Medium),
    Font(R.font.arima_semibold, FontWeight.SemiBold)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Arima,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.7.sp
    ),

    // Other default text styles to override

    titleLarge = TextStyle(
        fontFamily = ProtestStrike,
        fontSize = 32.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.7.sp,
        textAlign = TextAlign.Center
    ),

    titleMedium = TextStyle(
        fontFamily = ProtestStrike,
        fontSize = 28.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center
    ),

    labelSmall = TextStyle(
        fontFamily = Arima,
        fontWeight = FontWeight.ExtraLight,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)