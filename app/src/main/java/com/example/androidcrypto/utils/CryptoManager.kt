package com.example.androidcrypto.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class CryptoManager {

    private val keyStore = KeyStore.getInstance(KEYSTORE_TYPE).apply {
        load(null)
    }

    private val encryptCipher = Cipher.getInstance(TRANSFORMATION).apply {
        init(Cipher.ENCRYPT_MODE, getKey())
    }

    fun encrypt(bytes: ByteArray, outputStream: OutputStream): ByteArray {
        val encryptedBytes = encryptCipher.doFinal(bytes)
        outputStream.use {
            it.write(encryptCipher.iv.size)
            it.write(encryptCipher.iv)
            it.write(encryptedBytes.size)
            it.write(encryptedBytes)
        }
        return encryptedBytes
    }

    fun decrypt(inputStream: InputStream): ByteArray {
        return inputStream.use {
            val iv = it.getIv()
            val encryptedBytes = it.getEncryptedBytes()
            getDecryptCipherForIv(iv).doFinal(encryptedBytes)
        }
    }

    private fun getDecryptCipherForIv(iv: ByteArray) = Cipher.getInstance(TRANSFORMATION).apply {
        init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
    }

    private fun getKey(): SecretKey {
        val existingKey = keyStore.getEntry(KEYSTORE_ALIAS, null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(KeyGenParameterSpec.Builder(
                KEYSTORE_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).run {
                setBlockModes(BLOCK_MODE)
                setEncryptionPaddings(PADDING)
                setUserAuthenticationRequired(false)
                setRandomizedEncryptionRequired(true)
                build()
            })
        }.generateKey()
    }

    private fun InputStream.getEncryptedBytes(): ByteArray {
        val encryptedBytesSize = read()
        val encryptedBytes = ByteArray(encryptedBytesSize)
        read(encryptedBytes)
        return encryptedBytes
    }

    private fun InputStream.getIv(): ByteArray {
        val ivSize = read()
        val iv = ByteArray(ivSize)
        read(iv)
        return iv
    }

    companion object {
        private const val KEYSTORE_TYPE = "AndroidKeyStore"
        private const val KEYSTORE_ALIAS = "secret"
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }

}