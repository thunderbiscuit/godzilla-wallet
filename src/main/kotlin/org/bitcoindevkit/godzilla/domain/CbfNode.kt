package org.bitcoindevkit.godzilla.domain

import org.bitcoindevkit.Client
import org.bitcoindevkit.IpAddress
import org.bitcoindevkit.LightClientBuilder
import org.bitcoindevkit.Peer
import org.bitcoindevkit.ScanType
import org.bitcoindevkit.Warning

class CbfNode(private val wallet: Wallet) {
    var kyotoClient: Client? = null

    fun startKyoto() {
        println("Starting Kyoto node")
        val ip: IpAddress = IpAddress.fromIpv4(127u, 0u, 0u, 1u) // Regtest
        val peer1: Peer = Peer(ip, 18444u, false) // Regtest
        val peers: List<Peer> = listOf(peer1)

        val (client, node) = LightClientBuilder()
            .dataDir("./kyotodata/")
            .peers(peers)
            .connections(1u)
            .scanType(ScanType.New)
            .build(wallet.wallet)

        node.run()
        kyotoClient = client
        println("Kyoto node started")
    }

    suspend fun startSync(onUpdate: () -> Unit) {
        println("Starting sync")
        while (true) {
            val update = kyotoClient?.update()
            if (update == null) {
                println("UPDATE: Update is null")
            } else {
                println("UPDATE: Applying an update to the wallet")
                wallet.wallet.applyUpdate(update)
                onUpdate()
            }
            val balance = wallet.wallet.balance()
            println("New balance: ${balance.total.toBtc()}")
        }
    }

    suspend fun listenLogs() {
        while (true) {
            val nextLog: org.bitcoindevkit.Log = kyotoClient!!.nextLog()
            println("LOG: $nextLog")
        }
    }

    suspend fun listenWarnings() {
        while (true) {
            val nextWarning: Warning = kyotoClient!!.nextWarning()
            println("WARNING: $nextWarning")
        }
    }
}
