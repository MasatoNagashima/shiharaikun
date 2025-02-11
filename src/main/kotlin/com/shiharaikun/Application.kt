package com.shiharaikun

import com.shiharaikun.infra.invoice.PostgresInvoiceDataSource
import com.shiharaikun.infra.user.PostgresUserDataSource
import com.shiharaikun.plugins.configureMonitoring
import com.shiharaikun.plugins.configureSecurity
import com.shiharaikun.plugins.configureSerialization
import com.shiharaikun.presentation.auth.JwtTokenService
import com.shiharaikun.presentation.controller.configureRouting
import com.shiharaikun.presentation.hash.SHA256HashingService
import com.shiharaikun.usecase.auth.TokenConfig
import com.shiharaikun.usecase.invoice.*
import com.shiharaikun.usecase.user.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain
        .main(args)
}

fun Application.module() {
    val dotenv = dotenv()

    val hashingService = SHA256HashingService()
    val dbConfig =
        HikariConfig().apply {
            jdbcUrl = dotenv["DB_URL"]
            username = dotenv["DB_USER"]
            password = dotenv["DB_PASSWORD"]
            driverClassName = dotenv["DB_DRIVER"]
            maximumPoolSize = dotenv["DB_POOL_SIZE"].toInt()
        }
    val dataSource = HikariDataSource(dbConfig)
    Database.connect(dataSource)
    val userDataSource = PostgresUserDataSource(hashingService)
    val postgresInvoiceDataSource = PostgresInvoiceDataSource()
    val tokenService = JwtTokenService()
    val tokenConfig =
        TokenConfig(
            issuer = dotenv["JWT_ISSUER"],
            audience = dotenv["JWT_AUDIENCE"],
            expiresIn = dotenv["JWT_EXPIRES_IN"].toLong(),
            secret = dotenv["JWT_SECRET"],
        )
    val signUpUseCase = SignUpUseCase(userDataSource)
    val signInUseCase = SignInUseCase(userDataSource)
    val createInvoiceUseCase = CreateInvoiceUseCase(postgresInvoiceDataSource)
    val getInvoicesUseCase = GetInvoicesUseCase(postgresInvoiceDataSource)

    configureSerialization()
    configureMonitoring()
    configureSecurity(tokenConfig)
    configureRouting(tokenService, tokenConfig, signUpUseCase, signInUseCase, createInvoiceUseCase, getInvoicesUseCase)
}
