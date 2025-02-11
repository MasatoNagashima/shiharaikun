package com.shiharaikun.presentation.controller

import com.shiharaikun.usecase.auth.TokenClaim
import com.shiharaikun.usecase.auth.TokenConfig
import com.shiharaikun.usecase.auth.TokenService
import com.shiharaikun.usecase.user.AuthDto
import com.shiharaikun.usecase.user.SignInUseCase
import com.shiharaikun.usecase.user.SignUpUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.*
import io.ktor.server.request.receiveOrNull
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import kotlinx.serialization.Serializable

fun Route.signUp(signUpUseCase: SignUpUseCase) {
    post("users") {
        val request =
            call.receiveOrNull<SignUpRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

        val wasInserted =
            signUpUseCase.execute(
                name = request.name,
                email = request.email,
                password = request.password,
                companyName = request.companyName,
            )
        if (!wasInserted) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        call.respond(HttpStatusCode.OK)
    }
}

fun Route.signIn(
    signInUsecase: SignInUseCase,
    tokenService: TokenService,
    tokenConfig: TokenConfig,
) {
    post("signin") {
        val request =
            call.receiveOrNull<SignInRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
        val user =
            signInUsecase.execute(
                email = request.email,
                password = request.password,
            )

        if (user == null) {
            call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
            return@post
        }

        val token =
            tokenService.generate(
                config = tokenConfig,
                TokenClaim(
                    name = "userId",
                    value = user.userId.toString(),
                ),
            )

        call.respond(
            status = HttpStatusCode.OK,
            message =
                AuthDto(
                    token = token,
                ),
        )
    }
}

@Serializable
data class SignUpRequest(
    @NotEmpty
    val name: String,
    @field:Email
    val email: String,
    @field:Min(8) @NotEmpty
    val password: String,
    @NotEmpty
    val companyName: String,
)

@Serializable
data class SignInRequest(
    @field:Email
    val email: String,
    @field:Min(8) @NotEmpty
    val password: String,
)
