package com.shiharaikun.usecase.auth

interface TokenService {
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim,
    ): String
}
