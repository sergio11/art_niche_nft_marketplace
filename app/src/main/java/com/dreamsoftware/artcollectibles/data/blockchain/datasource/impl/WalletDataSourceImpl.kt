package com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.dreamsoftware.artcollectibles.BuildConfig
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IWalletDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.exception.GenerateWalletException
import com.dreamsoftware.artcollectibles.data.blockchain.exception.LoadWalletCredentialsException
import com.dreamsoftware.artcollectibles.data.blockchain.model.WalletDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.web3j.crypto.Credentials
import org.web3j.crypto.WalletUtils
import java.io.File
import java.security.Security

/**
 * Wallet Data Source Implementation
 * @param appContext
 */
internal class WalletDataSourceImpl(
    private val appContext: Context
) : IWalletDataSource {

    private companion object {
        const val INTERNAL_DIRECTORY_NAME = "wallets_internal_data"
    }

    init {
        setupBouncyCastle()
    }

    @Throws(LoadWalletCredentialsException::class)
    override suspend fun loadCredentials(name: String, password: String): Credentials =
        withContext(Dispatchers.IO) {
            try {
                WalletUtils.loadCredentials(password, File(getInternalWalletDirectory(), name))
            } catch (ex: Exception) {
                throw LoadWalletCredentialsException(
                    message = "An error occurred when loading credentials",
                    ex
                )
            }
        }

    @Throws(GenerateWalletException::class)
    override suspend fun generate(password: String): WalletDTO = withContext(Dispatchers.IO) {
        try {
            val walletName = WalletUtils.generateLightNewWalletFile(password, getInternalWalletDirectory())
            WalletDTO(walletName, getUriForWallet(walletName))
        } catch (ex: Exception) {
            throw GenerateWalletException(
                message = "An error occurred when creating a new wallet file",
                ex
            )
        }
    }

    private fun getInternalWalletDirectory(): File =
        File(appContext.filesDir, INTERNAL_DIRECTORY_NAME).also {
            if (!it.exists()) {
                it.mkdir()
            }
        }

    private fun getUriForWallet(walletName: String): Uri =
        FileProvider.getUriForFile(appContext, BuildConfig.APPLICATION_ID + ".provider",
            File(getInternalWalletDirectory(), walletName))

    private fun setupBouncyCastle() {
        val provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)
            ?: // Web3j will set up the provider lazily when it's first used.
            return
        if (provider.javaClass == BouncyCastleProvider::class.java) {
            // BC with same package name, shouldn't happen in real life.
            return
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        Security.insertProviderAt(BouncyCastleProvider(), 1)
    }
}