package com.example.androidcrypto.data

import kotlinx.serialization.Serializable

@Serializable
data class UserSettings(
    val username: String = "",
    val password: String = "",
)