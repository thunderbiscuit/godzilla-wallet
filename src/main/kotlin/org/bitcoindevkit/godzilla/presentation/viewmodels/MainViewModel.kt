package org.bitcoindevkit.godzilla.presentation.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.bitcoindevkit.godzilla.presentation.viewmodels.mvi.WalletAction
import org.bitcoindevkit.godzilla.presentation.viewmodels.mvi.WalletState

class MainViewModel() {
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    private val _walletState: MutableState<WalletState> = mutableStateOf(WalletState(kyotoReady = false))
    val walletState: MutableState<WalletState> get() = _walletState

    fun onAction(action: WalletAction) {
        when (action) {
            WalletAction.StartKyoto -> _walletState.value = _walletState.value.copy(kyotoReady = true)
        }
    }
}
