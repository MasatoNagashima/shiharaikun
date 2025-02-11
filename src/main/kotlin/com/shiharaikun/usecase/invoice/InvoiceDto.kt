package com.shiharaikun.usecase.invoice

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.math.BigDecimal
import java.time.LocalDate

@Serializable
data class InvoiceDto(
    val invoiceId: String,
    val userId: String,
    val issueDate: String,
    val paymentAmount: String,
    val feeRate: String, 
    val taxRate: String,
    val paymentDueDate: String
)