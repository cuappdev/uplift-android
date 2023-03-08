package com.cornellappdev.uplift.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

@Preview
@Composable
fun ClassDetailScreen(
    classDetailViewModel: ClassDetailViewModel = viewModel()
) {
    val upliftClass by classDetailViewModel.classFlow.collectAsState()
    Log.d("url", upliftClass?.imageUrl.toString())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Top Part
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = upliftClass?.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_back_arrow),
                contentDescription = null,
                modifier = Modifier
                    .align(
                        Alignment.TopStart
                    )
                    .padding(top = 47.dp, start = 22.dp),
                tint = Color.White
            )
            Image(
                painter = painterResource(id = if (upliftClass != null && upliftClass!!.isFavorite()) R.drawable.ic_star_filled else R.drawable.ic_star),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 47.dp, end = 21.dp)
                    .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                        upliftClass?.toggleFavorite()
                    }
            )
            // All text header information...
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = 42.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = upliftClass?.name?.uppercase() ?: "",
                    fontWeight = FontWeight(700),
                    fontSize = 48.sp,
                    lineHeight = 48.sp,
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.13.sp,
                    color = Color.White,
                    fontFamily = montserratFamily
                )
                Text(
                    text = upliftClass?.location ?: "",
                    fontWeight = FontWeight(400),
                    fontSize = 14.sp,
                    lineHeight = 17.07.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontFamily = montserratFamily
                )
                Spacer(Modifier.height(24.dp))
                Text(
                    text = upliftClass?.instructorName ?: "",
                    fontWeight = FontWeight(700),
                    fontSize = 16.sp,
                    lineHeight = 19.5.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontFamily = montserratFamily
                )
            }

            Surface(
                shape = CircleShape,
                color = Color.White,
                modifier = Modifier
                    .size(84.dp)
                    .offset(y = 42.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "${upliftClass?.minutes} MIN",
                        fontWeight = FontWeight(700),
                        fontSize = 14.sp,
                        lineHeight = 17.07.sp,
                        textAlign = TextAlign.Center,
                        color = PRIMARY_BLACK,
                        fontFamily = montserratFamily,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = 19.dp)
                    )
                }
            }
        }
    }
}