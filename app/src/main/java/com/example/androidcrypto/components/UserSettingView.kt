package com.example.androidcrypto.components

import android.content.Context
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.datastore.dataStore
import com.example.androidcrypto.CryptoManager
import com.example.androidcrypto.R
import com.example.androidcrypto.data.UserSettings
import com.example.androidcrypto.data.UserSettingsSerializer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private const val DATASTORE_FILENAME = "user-settings.json"

@Composable
fun UserSettingView() {

    val context = LocalContext.current

    var username by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var userSettings by remember {
        mutableStateOf(UserSettings())
    }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .padding(32.dp)
    ) {
        TextField(
            value = username,
            onValueChange = {
                username = it
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = stringResource(R.string.username))
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = {
                password = it
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = stringResource(R.string.password))
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(0.6F),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                coroutineScope.launch {
                    context.dataStore.updateData {
                        UserSettings(username, password)
                    }
                }
            }) {
                Text(text = stringResource(R.string.save))
            }
            Button(onClick = {
                coroutineScope.launch {
                    userSettings = context.dataStore.data.first()
                }
            }) {
                Text(text = stringResource(R.string.load))
            }
        }
        Text(text = userSettings.toString())
    }

}

private val Context.dataStore by dataStore(
    fileName = DATASTORE_FILENAME,
    serializer = UserSettingsSerializer(CryptoManager())
)
