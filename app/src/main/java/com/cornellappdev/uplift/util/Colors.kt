package com.cornellappdev.uplift.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

// To add a new color, do:
// [val colorName = Color(0xFF[...Hex Code Here...])]

val GRAY00 = Color(0xFFEFF1F4)
val GRAY01 = Color(0xFFE5ECED)
val GRAY02 = Color(0xFFA5A5A5)
val GRAY03 = Color(0xFFA1A5A6)
val GRAY04 = Color(0xFF707070)
val GRAY05 = Color(0xFF738390)
val LIGHT_YELLOW = Color(0xFFFCF5A4)
val PRIMARY_YELLOW = Color(0xFFF8E71C)
val PRIMARY_YELLOW_BACKGROUND = Color(0x0CF8E71C)
val PRIMARY_BLACK = Color(0xFF222222)
val ACCENT_OPEN = Color(0xFF64C270)
val ACCENT_CLOSED = Color(0xFFF07D7D)
val ACCENT_ORANGE = Color(0xFFFE8F13)

/**
 * Interpolates a color between [color1] and [color2] by choosing a color a [fraction] in between.
 * Uses HSV interpolation, which generally gives more aesthetically pleasing results than RGB.
 *
 * @param fraction  Float in [0..1]. 0 = color1, 1 = color2. In between interpolates between.
 */
fun colorInterp(fraction: Float, color1: Color, color2: Color): Color {
    val fractionToUse = fraction.coerceIn(0f, 1f)
    val HSV1 = FloatArray(3)
    val HSV2 = FloatArray(3)
    android.graphics.Color.colorToHSV(color1.toArgb(), HSV1)
    android.graphics.Color.colorToHSV(color2.toArgb(), HSV2)

    for (i in 0..2) {
        HSV2[i] = interpolate(fractionToUse, HSV1[i], HSV2[i])
    }
    return Color.hsv(
        HSV2[0],
        HSV2[1],
        HSV2[2],
        interpolate(fractionToUse, color1.alpha, color2.alpha)
    )
}

/**
 * Interpolates between two floats.
 */
private fun interpolate(fraction: Float, a: Float, b: Float): Float {
    return a + (b - a) * fraction
}
