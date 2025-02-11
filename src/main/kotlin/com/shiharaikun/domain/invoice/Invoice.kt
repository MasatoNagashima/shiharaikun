package com.shiharaikun.domain.invoice

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

data class Invoice(
    val invoiceId: String = UUID.randomUUID().toString(),
    val userId: String,
    val issueDate: LocalDate,
    val paymentAmount: BigDecimal, // 支払い金額
    val feeRate: BigDecimal = BigDecimal("4.0"), // 手数料率
    val taxRate: BigDecimal = BigDecimal("10.0"), // 消費税率
    val paymentDueDate: LocalDate, // 支払期日
    val isDeleted: Boolean = false,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
    val deletedAt: Instant? = null,
) {
    val fee: BigDecimal = (paymentAmount * feeRate / BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) // 手数料
    val taxAmount: BigDecimal = (fee * taxRate / BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) // 消費税
    val totalAmount: BigDecimal = (paymentAmount + fee + taxAmount).setScale(2, RoundingMode.HALF_UP) //  請求金額
}
