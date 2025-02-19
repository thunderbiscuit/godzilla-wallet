/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package org.bitcoindevkit.godzilla.presentation.ui

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.core.ModalBottomSheet
import com.composables.core.Scrim
import com.composables.core.Sheet
import com.composables.core.SheetDetent
import com.composables.core.SheetDetent.Companion.Hidden
import com.composables.core.rememberModalBottomSheetState
import org.bitcoindevkit.godzilla.generated.resources.Res
import org.bitcoindevkit.godzilla.generated.resources.godzilla
import org.jetbrains.compose.resources.painterResource
import com.composables.composetheme.ComposeTheme
import com.composables.composetheme.round
import com.composables.composetheme.roundFull
import com.composables.composetheme.roundL
import com.composables.composetheme.shapes
import com.composables.core.DragIndication
import com.composables.icons.lucide.Bitcoin
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.PencilLine
import com.composables.icons.lucide.Plus
import org.bitcoindevkit.godzilla.presentation.theme.GodzillaColors
import org.bitcoindevkit.godzilla.presentation.viewmodels.mvi.WalletAction
import org.bitcoindevkit.godzilla.presentation.viewmodels.mvi.WalletState

@Composable
@Preview
fun App(
    walletState: WalletState,
    onAction: (WalletAction) -> Unit,
) {
    val Peek = remember {
        SheetDetent("peek") { containerHeight, sheetHeight ->
            containerHeight * 0.6f
        }
    }

    val state = rememberModalBottomSheetState(
        initialDetent = Hidden,
        detents = listOf(Hidden, Peek)
    )
    if (walletState.closeBottomSheet) {
        if (state.currentDetent == Peek) {
            state.currentDetent = Hidden
        }
        onAction(WalletAction.BottomSheetClosed)
    }

    Row(
        Modifier.fillMaxSize().padding(bottom = 90.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(Res.drawable.godzilla),
            "Image of Godzilla",
            colorFilter = if (!walletState.kyotoReady) {
                ColorFilter.tint(Color.Gray.copy(alpha = 0.2f), BlendMode.Modulate)
            } else null,
            modifier = Modifier
                .height(120.dp)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            Modifier
                .clip(ComposeTheme.shapes.round)
                .clickable(role = Role.Button) { state.currentDetent = Peek }
                .align(Alignment.BottomEnd)
                .padding(36.dp)
        ) {
            Image(
                Lucide.Plus,
                null,
                colorFilter = ColorFilter.tint(GodzillaColors.MidGray),
                modifier = Modifier.size(42.dp),
            )
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        var value1 = remember { mutableStateOf("") }
        var value2 = remember { mutableStateOf("") }

        // Once a payment has been completed, we reset the text field values
        if (walletState.paymentCompleted) {
            value1.value = ""
            value2.value = ""
        }

        ModalBottomSheet(state = state) {
            Scrim(scrimColor = Color.Black.copy(0.3f), enter = fadeIn(), exit = fadeOut())
            Sheet(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .padding(horizontal = 56.dp)
                    .statusBarsPadding()
                    .padding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal).asPaddingValues())
                    .shadow(4.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(Color.White)
                    .widthIn(max = 740.dp)
                    .fillMaxWidth()
                    .imePadding(),
            ) {
                Box(Modifier.fillMaxWidth().height(600.dp), contentAlignment = Alignment.TopCenter) {
                    DragIndication(
                        modifier = Modifier.padding(top = 22.dp)
                            .background(Color.Black.copy(0.4f), ComposeTheme.shapes.roundFull)
                            .width(32.dp)
                            .height(4.dp)
                    )
                    SimpleTextFields(value1, value2)
                }

                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier.fillMaxWidth()
                        .height(600.dp)
                        .padding(bottom = 32.dp, end = 42.dp),
                ) {
                    Box(
                        Modifier
                            .clip(ComposeTheme.shapes.roundL)
                            .clickable(role = Role.Button) {
                                onAction(WalletAction.CreateSale(description = value1.value, amount = value2.value.toLong()))
                            }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        BasicText(text = "Create Sale", style = TextStyle(color = GodzillaColors.MidGray, fontSize = 14.sp, fontWeight = FontWeight(600)))
                    }
                }
            }
        }
    }
}

@Composable
fun SimpleTextFields(
    value1: MutableState<String>,
    value2: MutableState<String>,
) {
    var borderColor1 by remember { mutableStateOf(GodzillaColors.LightGray) }
    var borderDp1 by remember { mutableStateOf(1.dp) }

    var borderColor2 by remember { mutableStateOf(GodzillaColors.LightGray) }
    var borderDp2 by remember { mutableStateOf(1.dp) }

    Column(
        modifier = Modifier.padding(vertical = 80.dp, horizontal = 64.dp)
    ) {
        BasicTextField(
            value = value1.value,
            onValueChange = { value1.value = it },
            modifier = Modifier
                .onFocusChanged {
                    borderColor1 = if (it.isFocused) GodzillaColors.DarkGray else GodzillaColors.LightGray
                    borderDp1 = if (it.isFocused) 2.dp else 1.dp
                }
                .fillMaxWidth()
                .shadow(2.dp, RoundedCornerShape(4.dp))
                .background(Color.White, RoundedCornerShape(4.dp))
                .border(borderDp1, borderColor1, RoundedCornerShape(4.dp))
                .padding(vertical = 16.dp, horizontal = 12.dp),
            textStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight(400)),
            singleLine = true,
            decorationBox = { innerTextField ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(Lucide.PencilLine, null)

                    Box(contentAlignment = Alignment.CenterStart) {
                        if (value1.value.isBlank()) {
                            BasicText("Sale Description", style = TextStyle(color = GodzillaColors.LightGray))
                        }
                        innerTextField()
                    }
                }
            }
        )
        Spacer(Modifier.height(16.dp))
        BasicTextField(
            value = value2.value,
            onValueChange = { value2.value = it },
            modifier = Modifier
                .onFocusChanged {
                    borderColor2 = if (it.isFocused) GodzillaColors.DarkGray else GodzillaColors.LightGray
                    borderDp2 = if (it.isFocused) 2.dp else 1.dp
                }
                .fillMaxWidth()
                .shadow(2.dp, RoundedCornerShape(4.dp))
                .background(Color.White, RoundedCornerShape(4.dp))
                .border(borderDp2, borderColor2, RoundedCornerShape(4.dp))
                .padding(vertical = 16.dp, horizontal = 12.dp),
            textStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight(400)),
            singleLine = true,
            decorationBox = { innerTextField ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(Lucide.Bitcoin, null)

                    Box(contentAlignment = Alignment.CenterStart) {
                        if (value2.value.isBlank()) {
                            BasicText("Amount (Satoshis)", style = TextStyle(color = GodzillaColors.LightGray))
                        }
                        innerTextField()
                    }
                }
            }
        )
    }
}
