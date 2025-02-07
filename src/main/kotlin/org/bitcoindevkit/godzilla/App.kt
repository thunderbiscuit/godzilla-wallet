package org.bitcoindevkit.godzilla

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.composables.core.ModalBottomSheet
import com.composables.core.Scrim
import com.composables.core.Sheet
import com.composables.core.SheetDetent
import com.composables.core.SheetDetent.Companion.Hidden
import com.composables.core.rememberModalBottomSheetState
import kotlinx.coroutines.delay
import org.bitcoindevkit.godzilla.generated.resources.Res
import org.bitcoindevkit.godzilla.generated.resources.godzilla
import org.jetbrains.compose.resources.painterResource
import com.composables.composetheme.ComposeTheme
import com.composables.composetheme.round
import com.composables.composetheme.shapes
import org.bitcoindevkit.godzilla.presentation.viewmodels.MainViewModel

@Composable
@Preview
fun App(viewModel: MainViewModel) {
    val Peek = remember {
        SheetDetent("peek") { containerHeight, sheetHeight ->
            containerHeight * 0.6f
        }
    }

    val state = rememberModalBottomSheetState(
        initialDetent = Hidden,
        detents = listOf(Hidden, Peek)
    )

    Row(
        Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.godzilla),
            "Image of Godzilla",
            colorFilter = if (!viewModel.walletState.value.kyotoReady) {
                ColorFilter.tint(Color.Gray.copy(alpha = 0.2f), BlendMode.Modulate)
            } else null,
            modifier = Modifier
                .height(70.dp)
                .padding(end = 12.dp, top = 12.dp)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            Modifier
                .clip(ComposeTheme.shapes.round)
                .clickable(role = Role.Button) { state.currentDetent = Peek }
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Image(Icons.Rounded.Add, null, modifier = Modifier.size(42.dp), colorFilter = ColorFilter.tint(Color(0xFF424242)))
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isCompact = maxWidth < 600.dp

        LaunchedEffect(state.isIdle) {
            if (state.isIdle && state.targetDetent == Hidden) {
                delay(8000)
                state.currentDetent = Peek
            }
        }

        ModalBottomSheet(state = state) {
            Scrim(scrimColor = Color.Black.copy(0.3f), enter = fadeIn(), exit = fadeOut())
            Sheet(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .let { if (isCompact) it else it.padding(horizontal = 56.dp) }
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
                    // DragIndication(
                    //     modifier = Modifier.padding(top = 22.dp)
                    //         .background(Color.Black.copy(0.4f), ComposeTheme.shapes.roundFull)
                    //         .width(32.dp)
                    //         .height(4.dp)
                    // )
                }
            }
        }
    }
}
