package com.shiharaikun.usecase.user

import kotlinx.serialization.Serializable

@Serializable
data class AuthDto(
    val token: String,
)
