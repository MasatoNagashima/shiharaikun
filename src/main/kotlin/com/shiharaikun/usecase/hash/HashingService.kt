package com.shiharaikun.usecase.hash

interface HashingService {
    fun generateSaltedHash(
        value: String,
        saltLength: Int = 32,
    ): SaltedHash

    fun verify(
        value: String,
        saltedHash: SaltedHash,
    ): Boolean
}
