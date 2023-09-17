package com.cornellappdev.uplift.ui.screens.subscreens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cornellappdev.uplift.ui.components.general.LoadingTopBar
import com.cornellappdev.uplift.util.GRAY01
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainLoading(shimmer: Shimmer) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState, modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        stickyHeader {
            LoadingTopBar(shimmer)
        }

        // Spacer from header.
        item {
            Spacer(Modifier.height(24.dp))
        }

        // Mimic classes scroll part.
        item {
            Column {
                LoadingBlob(
                    width = 75.dp,
                    height = 12.dp,
                    shimmerInstance = shimmer,
                    cornerRadius = 6.dp,
                    paddingValues = PaddingValues(start = 16.dp, bottom = 19.dp)
                )
                LazyRow(state = rememberLazyListState()) {
                    item {
                        Spacer(Modifier.width(16.dp))
                    }

                    repeat(5) {
                        item {
                            LoadingBlob(
                                width = 228.dp,
                                height = 74.dp,
                                shimmerInstance = shimmer,
                                cornerRadius = 8.dp,
                                paddingValues = PaddingValues(end = 16.dp)
                            )
                        }
                    }
                }
            }
        }

        // Fake section to make it look better :-)
        item {
            Spacer(Modifier.height(36.dp))
            LoadingBlob(
                width = 135.dp,
                height = 12.dp,
                shimmerInstance = shimmer,
                cornerRadius = 6.dp,
                paddingValues = PaddingValues(start = 16.dp, bottom = 19.dp)
            )
            LazyRow(state = rememberLazyListState()) {
                item {
                    Spacer(Modifier.width(16.dp))
                }

                repeat(5) {
                    item {
                        LoadingBlob(
                            width = 275.dp,
                            height = 180.dp,
                            shimmerInstance = shimmer,
                            cornerRadius = 8.dp,
                            paddingValues = PaddingValues(end = 16.dp)
                        )
                    }
                }
            }
        }


        // Mimic Gyms section.
        item {
            Spacer(Modifier.height(36.dp))
            LoadingBlob(
                width = 135.dp,
                height = 12.dp,
                shimmerInstance = shimmer,
                cornerRadius = 6.dp,
                paddingValues = PaddingValues(start = 16.dp, bottom = 19.dp)
            )
            repeat(5) {
                LoadingBlob(
                    height = 180.dp,
                    shimmerInstance = shimmer,
                    cornerRadius = 12.dp,
                    paddingValues = PaddingValues(start = 16.dp, end = 16.dp, bottom = 12.dp)
                )
            }
        }
    }

}

/**
 * A loading blob that shimmers. Can be given a width and height, as well as a radius for its
 * rounded corner shape.
 */
@Composable
private fun LoadingBlob(
    width: Dp,
    height: Dp,
    shimmerInstance: Shimmer,
    cornerRadius: Dp,
    paddingValues: PaddingValues = PaddingValues()
) {
    Surface(
        color = GRAY01,
        modifier = Modifier
            .padding(paddingValues)
            .size(width = width, height = height)
            .shimmer(shimmerInstance),
        shape = RoundedCornerShape(cornerRadius)
    ) {}
}

/**
 * A loading blob that fills max size. Can be given a height, as well as a radius for its
 * rounded corner shape.
 */
@Composable
private fun LoadingBlob(
    height: Dp,
    shimmerInstance: Shimmer,
    cornerRadius: Dp,
    paddingValues: PaddingValues = PaddingValues()
) {
    Surface(
        color = GRAY01,
        modifier = Modifier
            .padding(paddingValues)
            .height(height)
            .shimmer(shimmerInstance)
            .fillMaxWidth(),
        shape = RoundedCornerShape(cornerRadius)
    ) {}
}
