package com.shiharaikun.usecase.hash

data class SaltedHash(
    val hash: String,
    val salt: String,
)
