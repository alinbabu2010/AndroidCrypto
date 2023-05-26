package com.example.androidcrypto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.androidcrypto.components.EncryptDecryptView
import com.example.androidcrypto.components.UserSettingView
import com.example.androidcrypto.ui.theme.AndroidCryptoTheme
import com.example.androidcrypto.utils.CryptoManager

class MainActivity : ComponentActivity() {

    private val cryptoManager = CryptoManager()

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidCryptoTheme {

                var isDataStoreUsage by remember {
                    mutableStateOf(false)
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    AnimatedContent(targetState = isDataStoreUsage, transitionSpec = {
                        (fadeIn() + slideInHorizontally()).with(fadeOut())
                    }) { isDataStoreUsage ->
                        if (isDataStoreUsage) UserSettingView()
                        else EncryptDecryptView(cryptoManager, filesDir)
                    }

                    ElevatedButton(
                        onClick = { isDataStoreUsage = !isDataStoreUsage },
                        elevation = ButtonDefaults.buttonElevation(8.dp)
                    ) {
                        Text(
                            text = stringResource(
                                if (isDataStoreUsage) R.string.basic_encryption
                                else R.string.datastore_encryption
                            )
                        )
                    }

                }

            }
        }
    }

}
