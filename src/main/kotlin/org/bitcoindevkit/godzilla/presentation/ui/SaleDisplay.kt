package org.bitcoindevkit.godzilla.presentation.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.composetheme.ComposeTheme
import com.composables.composetheme.round
import com.composables.composetheme.shapes
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.X
import org.bitcoindevkit.godzilla.presentation.theme.GodzillaColors
import org.bitcoindevkit.godzilla.presentation.viewmodels.mvi.WalletState

@Composable
fun SaleDisplay(
    walletState: WalletState,
    paymentCompleted: Boolean,
    closeDialog: () -> Unit,
) {
    val alpha by animateFloatAsState(
        targetValue = if (paymentCompleted) 0f else 1f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "Alpha"
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(alpha = alpha)
    ) {
        Box(
            Modifier
                .clip(ComposeTheme.shapes.round)
                .clickable(role = Role.Button) { closeDialog() }
                .align(Alignment.TopEnd)
                .padding(36.dp)
        ) {
            Image(
                Lucide.X,
                null,
                colorFilter = ColorFilter.tint(GodzillaColors.MidGray),
                modifier = Modifier
                    .size(36.dp),
            )
        }
        Box(
            Modifier
                .align(Alignment.Center)
                .padding(36.dp)
        ) {
            val imageBitmap = walletState.newSale?.third?.toComposeImageBitmap()
            if (imageBitmap != null) Image(
                bitmap = imageBitmap,
                contentDescription = "QR Code",
                modifier = Modifier.height(420.dp).width(420.dp)
            )
        }
        Column(Modifier.padding(start = 48.dp, top = 48.dp, end = 24.dp)) {
            BasicText(
                text = "Sale Description",
                style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp, color = GodzillaColors.DarkGray),
            )
            Spacer(Modifier.height(8.dp))
            BasicText(
                text = walletState.newSale?.first ?: "No description",
                style = TextStyle(color = GodzillaColors.MidGray, fontSize = 16.sp)
            )
            Spacer(Modifier.height(24.dp))
            BasicText(
                text = "Price",
                style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp, color = GodzillaColors.DarkGray)
            )
            Spacer(Modifier.height(8.dp))
            BasicText(
                text = "${walletState.newSale?.second.toString()} satoshis",
                style = TextStyle(color = GodzillaColors.MidGray, fontSize = 16.sp)
            )
        }
    }
}
