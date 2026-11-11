package com.cornellappdev.uplift.ui.components.general

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable

/**
 * AnimatedVisibilityContent is a composable that expands the content with an animation when it
 * is set to be visible.
 * @param visible whether the content is visible
 * @param content the content to be displayed
 */
@Composable
fun AnimatedVisibilityContent(
    visible: Boolean,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(animationSpec = tween(300)) + fadeIn(),
        exit = shrinkVertically(animationSpec = tween(300)) + fadeOut()
    ) {
        content()
    }
}