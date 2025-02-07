package org.bitcoindevkit.godzilla.presentation.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bitcoindevkit.godzilla.domain.CbfNode
import org.bitcoindevkit.godzilla.domain.Wallet
import org.bitcoindevkit.godzilla.presentation.viewmodels.mvi.WalletAction
import org.bitcoindevkit.godzilla.presentation.viewmodels.mvi.WalletState

class MainViewModel() {
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    private val _walletState: MutableState<WalletState> = mutableStateOf(WalletState(kyotoReady = false))
    val walletState: MutableState<WalletState> get() = _walletState

    val wallet: Wallet = Wallet()
    val cbfNode = CbfNode(wallet)

    fun onAction(action: WalletAction) {
        when (action) {
            WalletAction.StartKyoto -> startKyoto()
        }
    }

    private fun startKyoto() {
        cbfNode.startKyoto()
        _walletState.value = _walletState.value.copy(kyotoReady = true)

        coroutineScope.launch {
            cbfNode.startSync()
        }
        coroutineScope.launch {
            cbfNode.listenLogs()
        }
        coroutineScope.launch {
            cbfNode.listenWarnings()
        }
    }
}
