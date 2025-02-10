package org.bitcoindevkit.godzilla.presentation.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.composables.core.DialogState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bitcoindevkit.KeychainKind
import org.bitcointools.bip21.Bip21URI
import org.bitcoindevkit.godzilla.domain.CbfNode
import org.bitcoindevkit.godzilla.domain.Wallet
import org.bitcoindevkit.godzilla.domain.generateQRCodeImage
import org.bitcoindevkit.godzilla.presentation.viewmodels.mvi.WalletAction
import org.bitcoindevkit.godzilla.presentation.viewmodels.mvi.WalletState
import org.bitcointools.bip21.parameters.Amount
import org.bitcointools.bip21.parameters.Label

class MainViewModel(private val dialogState: DialogState) {
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    private val _walletState: MutableState<WalletState> = mutableStateOf(WalletState(kyotoReady = false, closeBottomSheet = false))
    val walletState: MutableState<WalletState> get() = _walletState

    private val wallet: Wallet = Wallet()
    private val cbfNode = CbfNode(wallet)

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

        val address: String = wallet.wallet.revealNextAddress(KeychainKind.EXTERNAL).address.toString()
        val bip21Amount: Amount = Amount(amount.toLong())
        val label: Label = Label(description)

        val bip21String: Bip21URI = Bip21URI(address, bip21Amount, label)
        println(bip21String)

        val qrCode = generateQRCodeImage(bip21String.toURI(), 600, 600)
        _walletState.value = _walletState.value.copy(
            closeBottomSheet = true,
            newSale = Triple(description, amount, qrCode)
        )
    }

    private fun bottomSheetClosed() {
        _walletState.value = _walletState.value.copy(closeBottomSheet = false)
    }
}
