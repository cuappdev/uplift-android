package com.cornellappdev.uplift.ui.components.gymdetail

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.models.UpliftCapacity
import com.cornellappdev.uplift.models.UpliftGym
import com.cornellappdev.uplift.networking.ApiResponse
import com.cornellappdev.uplift.ui.components.general.FavoriteButton
import com.cornellappdev.uplift.util.ACCENT_CLOSED
import com.cornellappdev.uplift.util.ACCENT_OPEN
import com.cornellappdev.uplift.util.ACCENT_ORANGE
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY03
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.colorInterp
import com.cornellappdev.uplift.util.isOpen
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun GymDetailHero(
    scrollState: ScrollState,
    bitmapState: MutableState<ApiResponse<ImageBitmap>>,
    screenHeightPx: Float,
    progress: Float,
    onBack: () -> Unit,
    gym: UpliftGym?,
    day: Int,
    capacity: UpliftCapacity?,
    onRefresh: () -> Unit = {}
) {
    val grayedOut = !isOpen(gym!!.hours[day]) || capacity == null

    val fraction = if (!grayedOut) {
        (capacity!!.percent.toFloat()) / 2
    } else if (!isOpen(gym.hours[day])) 0.5f
    else 0f

    val animatedFraction = remember { Animatable(0f) }
    val orangeCutoff = .325f
    val color =
        if (!isOpen(gym.hours[day])) {
            ACCENT_CLOSED
        } else if (fraction > orangeCutoff)
            colorInterp(
                (fraction - orangeCutoff) / (1 - orangeCutoff),
                ACCENT_ORANGE,
                ACCENT_CLOSED
            )
        else
            colorInterp(
                fraction / orangeCutoff,
                ACCENT_OPEN,
                ACCENT_ORANGE
            )

    // When the composable launches, animate the fraction to the capacity fraction.
    LaunchedEffect(animatedFraction) {
        animatedFraction.animateTo(
            fraction,
            animationSpec = tween(durationMillis = 750)
        )
    }
    Box(modifier = Modifier
        .fillMaxWidth()
        .graphicsLayer {
            translationY = 0.5f * scrollState.value
        })
    {
        GymImage(bitmapState, scrollState, screenHeightPx, progress)

        // Back Button
        Icon(
            painter = painterResource(id = R.drawable.ic_back_arrow),
            contentDescription = null,
            modifier = Modifier
                .align(
                    Alignment.TopStart
                )
                .padding(top = 47.dp, start = 22.dp)
                .clip(RoundedCornerShape(4.dp))
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = onBack
                ),
            tint = Color.White
        )

        // Favorite Button
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 47.dp, end = 21.dp)
        ) {
            FavoriteButton(
                filled = (gym != null && gym!!.isFavorite())
            ) { gym?.toggleFavorite() }
        }

        // Gym Name
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = gym?.name?.uppercase() ?: "",
            fontWeight = FontWeight(700),
            fontSize = 36.sp,
            lineHeight = 44.sp,
            textAlign = TextAlign.Center,
            letterSpacing = 1.13.sp,
            color = Color.White,
            fontFamily = montserratFamily
        )

        Surface(
            shape = CircleShape,
            modifier = Modifier
                .size(180.dp)
                .offset(y = 90.dp)
                .align(Alignment.BottomCenter)
                .graphicsLayer {
                    translationY = -0.5f * scrollState.value.toFloat()
                },
            color = Color.Transparent
        ) {
            CapacityCircle(animatedFraction, color, capacity, isOpen(gym.hours[day]), onRefresh)
        }


    }
}

@Composable
private fun GymImage(
    bitmapState: MutableState<ApiResponse<ImageBitmap>>,
    scrollState: ScrollState,
    screenHeightPx: Float,
    progress: Float
) {
    Crossfade(
        targetState = bitmapState.value,
        label = "imageFade",
        animationSpec = tween(250)
    ) { apiResponse ->
        when (apiResponse) {
            is ApiResponse.Success ->
                Image(
                    bitmap = apiResponse.data,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .graphicsLayer {
                            alpha = 1 - (scrollState.value.toFloat() / screenHeightPx)
                        },
                    contentScale = ContentScale.Crop,
                )

            else ->
                Image(
                    bitmap = ImageBitmap(height = 1, width = 1),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .graphicsLayer {
                            alpha = 1 - (scrollState.value.toFloat() / screenHeightPx)
                        }
                        .background(colorInterp(progress, GRAY01, GRAY03)),
                    contentScale = ContentScale.Crop,
                )
        }
    }
}

@Composable
private fun CapacityCircle(
    animatedFraction: Animatable<Float, AnimationVector1D>,
    color: Color,
    capacity: UpliftCapacity?,
    open: Boolean,
    onRefresh: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { animatedFraction.value },
            modifier = Modifier
                .size(180.dp)
                .rotate(270f),
            color = color,
            strokeWidth = 12.dp,
            trackColor = Color.Transparent,
        )
        Box(
            modifier = Modifier
                .size(161.dp)
                .background(
                    color = Color.White,
                    shape = CircleShape
                )
                .offset(y = (-36).dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                Text(
                    text = if (open) "${capacity?.percentString() ?: "0%"} Full" else "CLOSED",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = montserratFamily,
                    color = color,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.clickable { onRefresh() }
                ) {
                    Text(
                        text = capacity?.updatedString() ?: "N/A",
                        fontFamily = montserratFamily,
                        fontSize = 14.sp,
                        color = GRAY04,
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_refresh),
                        contentDescription = null,
                        tint = GRAY04,
                        modifier = Modifier.size(14.dp)
                    )

                }

            }
        }
    }
}
