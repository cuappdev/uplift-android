package com.cornellappdev.uplift.ui.components.general

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.cornellappdev.uplift.util.GRAY01
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.shimmer

/**
 * Shared rendering for Home and Profile skeletons, extracted from Home's LoadingBlob.
 * Keep the existing gray Surface and apply shimmer to each placeholder; the caller supplies
 * the shared window-bounded animation so all placeholders use the same theme and timing.
 */
@Composable
fun LoadingPlaceholder(
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(6.dp)
) {
    Surface(
        color = GRAY01,
        modifier = modifier.shimmer(shimmer),
        shape = shape
    ) {}
}
