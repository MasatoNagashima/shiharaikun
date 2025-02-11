package com.shiharaikun.presentation.controller

import com.shiharaikun.usecase.auth.TokenConfig
import com.shiharaikun.usecase.auth.TokenService
import com.shiharaikun.usecase.invoice.CreateInvoiceUseCase
import com.shiharaikun.usecase.invoice.GetInvoicesUseCase
import com.shiharaikun.usecase.user.SignInUseCase
import com.shiharaikun.usecase.user.SignUpUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    tokenService: TokenService,
    tokenConfig: TokenConfig,
    signUpUseCase: SignUpUseCase,
    signInUseCase: SignInUseCase,
    createInvoiceUseCase: CreateInvoiceUseCase,
    getInvoicesUseCase: GetInvoicesUseCase,
) {
    routing {
        signIn(signInUseCase, tokenService, tokenConfig)
        signUp(signUpUseCase)
        createInvoice(createInvoiceUseCase)
        getInvoices(getInvoicesUseCase)
    }
}
