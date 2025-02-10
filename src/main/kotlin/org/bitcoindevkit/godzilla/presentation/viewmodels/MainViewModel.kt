package org.bitcoindevkit.godzilla.presentation.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.composables.core.DialogState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bitcoindevkit.Address
import org.bitcoindevkit.CanonicalTx
import org.bitcoindevkit.KeychainKind
import org.bitcointools.bip21.Bip21URI
import org.bitcoindevkit.godzilla.domain.CbfNode
import org.bitcoindevkit.godzilla.domain.Wallet
import org.bitcoindevkit.godzilla.domain.generateQRCodeImage
import org.bitcoindevkit.godzilla.presentation.viewmodels.mvi.WalletAction
import org.bitcoindevkit.godzilla.presentation.viewmodels.mvi.WalletState
import org.bitcointools.bip21.parameters.Amount
import org.bitcointools.bip21.parameters.Label
import org.rustbitcoin.bitcoin.Network

class MainViewModel(private val dialogState: DialogState) {
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    private val _walletState: MutableState<WalletState> = mutableStateOf(WalletState(kyotoReady = false, closeBottomSheet = false))
    val walletState: MutableState<WalletState> get() = _walletState

    private val wallet: Wallet = Wallet()
    private val cbfNode = CbfNode(wallet)

    private var waitingForPayment: String? = null

    fun onAction(action: WalletAction) {
        when (action) {
            is WalletAction.StartKyoto -> startKyoto()
            is WalletAction.CreateSale -> createSale(action.description, action.amount)
            is WalletAction.BottomSheetClosed -> bottomSheetClosed()
            is WalletAction.ReadyForNewPayment -> readyForNewPayment()
        }
    }

    private fun startKyoto() {
        cbfNode.startKyoto()
        _walletState.value = _walletState.value.copy(kyotoReady = true)

        coroutineScope.launch {
            cbfNode.startSync(::checkPayments)
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

        waitingForPayment(address)

        val qrCode = generateQRCodeImage(bip21String.toURI(), 600, 600)
        _walletState.value = _walletState.value.copy(
            closeBottomSheet = true,
            newSale = Triple(description, amount, qrCode)
        )
    }

    private fun waitingForPayment(address: String) {
        waitingForPayment = address


    }

    private fun bottomSheetClosed() {
        _walletState.value = _walletState.value.copy(closeBottomSheet = false)
    }

    private fun checkPayments() {
        if (waitingForPayment != null) {
            val transactions: List<CanonicalTx> = wallet.wallet.transactions()
            println("Looping over transactions to find the payment: $transactions")
            println("The wallet currently has ${transactions.size} transactions")

            transactions.forEach { transaction ->
                println("This transaction: $transaction")
                val outputs = transaction.transaction.output()
                outputs.forEach { output ->
                    println("Output: $output")
                    if (output.value != 0uL) {
                        println("Script: ${output.scriptPubkey}")
                        println("Address: ${Address.fromScript(output.scriptPubkey, Network.REGTEST)}")

                        val outputAddress: String = Address.fromScript(output.scriptPubkey, Network.REGTEST).toString()

                        if (waitingForPayment == outputAddress) {
                            waitingForPayment = null
                            _walletState.value = _walletState.value.copy(paymentCompleted = true)
                        }
                    }
                }
            }
        }
    }

    private fun readyForNewPayment() {
        _walletState.value = _walletState.value.copy(paymentCompleted = false)
    }
}
