/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package org.bitcoindevkit.godzilla.presentation.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.CircleCheckBig
import com.composables.icons.lucide.Lucide
import org.bitcoindevkit.godzilla.presentation.theme.GodzillaColors

@Composable
fun PaymentSuccess(isVisible: Boolean) {
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "Alpha"
    )

    val offsetX by animateDpAsState(
        targetValue = if (isVisible) 0.dp else (90).dp, // Moves in from right
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "OffsetX"
    )

    if (isVisible) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .offset(x = offsetX)
                .graphicsLayer(
                    alpha = alpha
                )
        ) {
            Icon(
                imageVector = Lucide.CircleCheckBig,
                contentDescription = "Success",
                tint = GodzillaColors.GodzillaGreen,
                modifier = Modifier
                    .size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Thank you!",
                style = TextStyle(fontSize = 42.sp, color = GodzillaColors.LightGray),
            )
        }
    }
}
