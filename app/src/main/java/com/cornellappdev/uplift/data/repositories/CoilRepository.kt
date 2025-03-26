package com.cornellappdev.uplift.data.repositories

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import coil.request.ImageRequest
import com.cornellappdev.uplift.data.models.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Handles background image URL loading.
 */
object CoilRepository {
    private val urlMap: MutableMap<String, MutableState<ApiResponse<ImageBitmap>>> = mutableMapOf()

    /**
     * Returns a [MutableState] containing an [ApiResponse] corresponding to a loading or loaded
     * image bitmap for loading the input [imageUrl]. If the image previously resulted in an error,
     * calling this function will attempt to re-load.
     *
     * Loads images with Coil.
     */
    fun getUrlState(imageUrl: String, context: Context): MutableState<ApiResponse<ImageBitmap>> {
        val imageLoader = context.imageLoader

        // Make new request.
        if (!urlMap.containsKey(imageUrl) || urlMap[imageUrl]!!.value is ApiResponse.Error) {
            val imageRequest = ImageRequest.Builder(context)
                .data(imageUrl)
                .build()

            urlMap[imageUrl] = mutableStateOf(ApiResponse.Loading)

            val disposable = imageLoader.enqueue(imageRequest)

            CoroutineScope(Dispatchers.IO).launch {
                val result = disposable.job.await()
                if (result.drawable == null) {
                    urlMap[imageUrl]!!.value = ApiResponse.Error
                } else {
                    urlMap[imageUrl]!!.value =
                        ApiResponse.Success(result.drawable!!.toBitmap().asImageBitmap())
                }
            }
        }

        return urlMap[imageUrl]!!
    }
}
