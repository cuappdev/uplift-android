package com.cornellappdev.uplift.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */

)

val Montserrat = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_bold, FontWeight.Bold),
)

object AppTextStyles {
    val BodySemibold = TextStyle(
        fontFamily = Montserrat,
        fontSize = 14.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.SemiBold
    )

    val LabelNormal = TextStyle(
        fontFamily = Montserrat,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Normal
    )

    val LabelMedium = TextStyle(
        fontFamily = Montserrat,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Medium
    )

    val LabelBig= TextStyle(
        fontFamily = Montserrat,
        fontSize = 14.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Medium
    )
}