package org.bitcoindevkit.godzilla.presentation.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.composables.core.DialogState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bitcoindevkit.godzilla.domain.CbfNode
import org.bitcoindevkit.godzilla.domain.Wallet
import org.bitcoindevkit.godzilla.presentation.viewmodels.mvi.WalletAction
import org.bitcoindevkit.godzilla.presentation.viewmodels.mvi.WalletState

class MainViewModel(val dialogState: DialogState) {
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    private val _walletState: MutableState<WalletState> = mutableStateOf(WalletState(kyotoReady = false, closeBottomSheet = false))
    val walletState: MutableState<WalletState> get() = _walletState

    val wallet: Wallet = Wallet()
    val cbfNode = CbfNode(wallet)

    fun onAction(action: WalletAction) {
        when (action) {
            is WalletAction.StartKyoto -> startKyoto()
            is WalletAction.CreateSale -> createSale(action.description, action.amount)
            is WalletAction.BottomSheetClosed -> bottomSheetClosed()
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

    private fun createSale(description: String, amount: ULong) {
        dialogState.visible = true
        _walletState.value = _walletState.value.copy(
            closeBottomSheet = true,
            newSale = Pair(description, amount)
        )
    }

    private fun bottomSheetClosed() {
        _walletState.value = _walletState.value.copy(closeBottomSheet = false)
    }
}
