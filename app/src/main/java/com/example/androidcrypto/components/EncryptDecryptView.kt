package com.example.androidcrypto.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.androidcrypto.R
import com.example.androidcrypto.utils.CryptoManager
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

private const val FILENAME = "secret.txt"

@Composable
fun EncryptDecryptView(cryptoManager: CryptoManager, filesDir: File) {

    var messageToEncrypt by remember {
        mutableStateOf("")
    }

    var messageToDecrypt by remember {
        mutableStateOf("")
    }

    fun getDecryptedMessage(): String {
        val file = File(filesDir, FILENAME)
        return cryptoManager.decrypt(FileInputStream(file)).decodeToString()
    }

    fun getEncryptedMessage(messageToEncrypt: String): String {
        val bytes = messageToEncrypt.trim().encodeToByteArray()
        val file = File(filesDir, FILENAME)
        if (!file.exists()) {
            file.createNewFile()
        }
        return cryptoManager.encrypt(bytes, FileOutputStream(file)).decodeToString()
    }

    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(32.dp)
    ) {
        TextField(
            value = messageToEncrypt,
            onValueChange = {
                messageToEncrypt = it
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = stringResource(R.string.encrypt_string))
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(0.6F),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                messageToDecrypt = getEncryptedMessage(messageToEncrypt)
            }) {
                Text(text = stringResource(R.string.encrypt))
            }
            Button(onClick = {
                messageToEncrypt = getDecryptedMessage()
            }) {
                Text(text = stringResource(R.string.decrypt))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(messageToDecrypt)
    }

}