package org.bitcoindevkit.godzilla.presentation.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.composables.core.Dialog
import com.composables.core.DialogPanel
import com.composables.core.DialogProperties
import com.composables.core.Scrim
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.X
import org.bitcoindevkit.godzilla.presentation.viewmodels.mvi.WalletState
import com.composables.core.DialogState
import kotlinx.coroutines.delay

@Composable
fun NewSaleDialog(
    dialogState: DialogState,
    walletState: WalletState
) {
    Box {
        Dialog(
            properties = DialogProperties(dismissOnClickOutside = true, dismissOnBackPress = false),
            state = dialogState
        ) {
            Scrim(scrimColor = Color.Black.copy(0.3f), exit = fadeOut(tween(durationMillis = 300)))
            DialogPanel(
                enter = scaleIn(initialScale = 0.4f) + fadeIn(tween(durationMillis = 600)),
                exit = scaleOut(targetScale = 0.6f) + fadeOut(tween(durationMillis = 300)),
                modifier = Modifier
                    .displayCutoutPadding()
                    .systemBarsPadding()
                    .widthIn(min = 280.dp, max = 900.dp)
                    .padding(20.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFE4E4E4), RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFFFFF))
            ) {

                var paymentCompleted by remember { mutableStateOf(false) }
                var showSuccess by remember { mutableStateOf(false) }

                LaunchedEffect(paymentCompleted) {
                    if (paymentCompleted) {
                        delay(700)
                        showSuccess = true
                    }
                }

                Column(
                    Modifier.height(600.dp).width(900.dp)
                ) {
                    AnimatedSuccess(showSuccess)
                    SaleData(
                        walletState = walletState,
                        paymentCompleted = paymentCompleted,
                        dialogState = dialogState
                    )
                }
            }
        }
    }
}

@Composable
fun SaleData(
    walletState: WalletState,
    paymentCompleted: Boolean,
    dialogState: DialogState
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
                .clickable(role = Role.Button) { dialogState.visible = false }
                .align(Alignment.TopEnd)
                .padding(36.dp)
        ) {
            Image(
                Lucide.X,
                null,
                colorFilter = ColorFilter.tint(Color(0xFF424242)),
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
                style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp)
            )
            Spacer(Modifier.height(8.dp))
            BasicText(
                text = walletState.newSale?.first ?: "No description",
                style = TextStyle(color = Color(0xFF474747), fontSize = 16.sp)
            )
            Spacer(Modifier.height(24.dp))
            BasicText(
                text = "Price",
                style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp)
            )
            Spacer(Modifier.height(8.dp))
            BasicText(
                text = "${walletState.newSale?.second.toString()} satoshis",
                style = TextStyle(color = Color(0xFF474747), fontSize = 16.sp)
            )
        }
    }
}
