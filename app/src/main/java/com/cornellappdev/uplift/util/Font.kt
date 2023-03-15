package com.cornellappdev.uplift.util

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight.Companion.Black
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.ExtraBold
import androidx.compose.ui.text.font.FontWeight.Companion.ExtraLight
import androidx.compose.ui.text.font.FontWeight.Companion.Light
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.font.FontWeight.Companion.Thin
import com.cornellappdev.uplift.R

val montserratFamily = FontFamily(
    Font(R.font.monsterrat_extra_bold, ExtraBold),
    Font(R.font.monsterrat_extra_bold_italic, ExtraBold, Italic),
    Font(R.font.monsterrat_extra_light, ExtraLight),
    Font(R.font.monsterrat_italic, ExtraLight),
    Font(R.font.monsterrat_medium, Medium),
    Font(R.font.monsterrat_semibold, SemiBold),
    Font(R.font.montserrat_black, Black),
    Font(R.font.montserrat_black_italic, Black, Italic),
    Font(R.font.montserrat_bold, Bold),
    Font(R.font.montserrat_bold_italic, Bold, Italic),
    Font(R.font.montserrat_extra_light_italic, ExtraLight, Italic),
    Font(R.font.montserrat_light, Light),
    Font(R.font.montserrat_light_italic, Light, Italic),
    Font(R.font.montserrat_medium_italic, Medium, Italic),
    Font(R.font.montserrat_regular),
    Font(R.font.montserrat_semibold_italic, SemiBold, Italic),
    Font(R.font.montserrat_thin, Thin),
    Font(R.font.montserrat_thin_italic, Thin, Italic),
)

val bebasNeueFamily = FontFamily(
    Font(R.font.bebas_neue_regular)
)