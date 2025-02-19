/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package org.bitcoindevkit.godzilla.presentation.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.composables.core.Dialog
import com.composables.core.DialogPanel
import com.composables.core.DialogProperties
import com.composables.core.Scrim
import org.bitcoindevkit.godzilla.presentation.viewmodels.mvi.WalletState
import com.composables.core.DialogState
import kotlinx.coroutines.delay
import org.bitcoindevkit.godzilla.presentation.viewmodels.mvi.WalletAction

@Composable
fun NewSaleDialog(
    dialogState: DialogState,
    walletState: WalletState,
    onAction: (WalletAction) -> Unit
) {
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
                .background(Color.White)
        ) {

            var showSuccess by remember { mutableStateOf(false) }

            LaunchedEffect(walletState.paymentCompleted) {
                if (walletState.paymentCompleted) {
                    delay(700)
                    showSuccess = true
                    delay(3000)
                    onAction(WalletAction.DismissDialog)
                    onAction(WalletAction.ReadyForNewPayment)
                }

            }

            Column(
                Modifier.height(600.dp).width(900.dp)
            ) {
                PaymentSuccess(showSuccess)
                SaleDisplay(
                    walletState = walletState,
                    paymentCompleted = walletState.paymentCompleted,
                    onAction = onAction,
                )
            }
        }
    }
}
