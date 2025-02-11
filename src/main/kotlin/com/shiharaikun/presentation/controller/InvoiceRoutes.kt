package com.shiharaikun.presentation.controller

import com.shiharaikun.usecase.invoice.CreateInvoiceUseCase
import com.shiharaikun.usecase.invoice.GetInvoicesUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.request.receiveOrNull
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import kotlinx.serialization.Serializable

fun Route.createInvoice(createInvoiceUseCase: CreateInvoiceUseCase) {
    authenticate {
        post("invoices") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized, "User not authorized")
                return@post
            }

            val request =
                call.receiveOrNull<CreateInvoiceRequest>() ?: kotlin.run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }

            val wasInserted =
                createInvoiceUseCase.execute(
                    userId = userId,
                    issueDateString = request.issueDate,
                    paymentAmountString = request.paymentAmount,
                    paymentDueDateString = request.paymentDueDate,
                )
            if (!wasInserted) {
                call.respond(HttpStatusCode.Conflict)
                return@post
            }
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.getInvoices(getInvoicesUseCase: GetInvoicesUseCase) {
    authenticate {
        get("invoices") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized, "User not authorized")
                return@get
            }

            val startDateString = call.request.queryParameters["startDate"]
            val endDateString = call.request.queryParameters["endDate"]
            val invoices =
                getInvoicesUseCase.execute(
                    userId,
                    startDateString,
                    endDateString,
                )

            if (invoices == null) {
                call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
                return@get
            }
            call.respond(
                status = HttpStatusCode.OK,
                message = invoices,
            )
        }
    }
}

@Serializable
data class CreateInvoiceRequest(
    @NotEmpty
    val issueDate: String,
    @Min(0) @NotEmpty
    val paymentAmount: String,
    @NotEmpty
    val paymentDueDate: String,
)
