package com.shiharaikun.presentation.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.shiharaikun.usecase.auth.TokenClaim
import com.shiharaikun.usecase.auth.TokenConfig
import com.shiharaikun.usecase.auth.TokenService
import java.util.*

class JwtTokenService : TokenService {
    override fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim,
    ): String {
        var token =
            JWT
                .create()
                .withAudience(config.audience)
                .withIssuer(config.issuer)
                .withExpiresAt(Date(System.currentTimeMillis() + config.expiresIn))
        claims.forEach { claim ->
            token = token.withClaim(claim.name, claim.value)
        }
        return token.sign(Algorithm.HMAC256(config.secret))
    }
}
