package com.example.androidcrypto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.androidcrypto.components.EncryptDecryptView
import com.example.androidcrypto.ui.theme.AndroidCryptoTheme

class MainActivity : ComponentActivity() {

    private val cryptoManager = CryptoManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidCryptoTheme {
                EncryptDecryptView(cryptoManager, filesDir)
            }
        }
    }

}
