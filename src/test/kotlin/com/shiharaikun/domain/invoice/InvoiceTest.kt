package com.shiharaikun.domain.invoice

import com.shiharaikun.domain.invoice.Invoice
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.assertEquals

class InvoiceTest {
    @Test
    fun `should calculate fee correctly`() {
        val invoice =
            Invoice(
                userId = "user123",
                issueDate = LocalDate.now(),
                paymentAmount = BigDecimal("1000"),
                paymentDueDate = LocalDate.now().plusDays(30),
            )

        // 手数料は 1000 * 4% = 40
        val expectedFee = BigDecimal("40.00")
        assertEquals(expectedFee, invoice.fee)
    }

    @Test
    fun `should calculate tax amount correctly`() {
        val invoice =
            Invoice(
                userId = "user123",
                issueDate = LocalDate.now(),
                paymentAmount = BigDecimal("1000"),
                paymentDueDate = LocalDate.now().plusDays(30),
            )

        // 手数料は 1000 * 4% = 40
        // 消費税は 40 * 10% = 4
        val expectedTaxAmount = BigDecimal("4.00")
        assertEquals(expectedTaxAmount, invoice.taxAmount)
    }

    @Test
    fun `should calculate total amount correctly`() {
        val invoice =
            Invoice(
                userId = "user123",
                issueDate = LocalDate.now(),
                paymentAmount = BigDecimal("1000"),
                paymentDueDate = LocalDate.now().plusDays(30),
            )

        // 手数料 = 1000 * 4% = 40
        // 消費税 = 40 * 10% = 4
        // 合計金額 = 1000 + 40 + 4 = 1044
        val expectedTotalAmount = BigDecimal("1044.00")
        assertEquals(expectedTotalAmount, invoice.totalAmount)
    }

    @Test
    fun `should use default fee rate and tax rate`() {
        val invoice =
            Invoice(
                userId = "user123",
                issueDate = LocalDate.now(),
                paymentAmount = BigDecimal("1000"),
                paymentDueDate = LocalDate.now().plusDays(30),
            )

        // デフォルトの手数料率 4%、消費税率 10%
        val expectedFee = BigDecimal("40.00")
        val expectedTaxAmount = BigDecimal("4.00")
        val expectedTotalAmount = BigDecimal("1044.00")

        assertEquals(expectedFee, invoice.fee)
        assertEquals(expectedTaxAmount, invoice.taxAmount)
        assertEquals(expectedTotalAmount, invoice.totalAmount)
    }

    @Test
    fun `should handle custom fee rate and tax rate`() {
        val invoice =
            Invoice(
                userId = "user123",
                issueDate = LocalDate.now(),
                paymentAmount = BigDecimal("1000"),
                feeRate = BigDecimal("5.0"), // カスタム手数料率 5%
                taxRate = BigDecimal("8.0"), // カスタム消費税率 8%
                paymentDueDate = LocalDate.now().plusDays(30),
            )

        // 手数料 = 1000 * 5% = 50
        // 消費税 = 50 * 8% = 4
        // 合計金額 = 1000 + 50 + 4 = 1054
        val expectedFee = BigDecimal("50.00")
        val expectedTaxAmount = BigDecimal("4.00")
        val expectedTotalAmount = BigDecimal("1054.00")

        assertEquals(expectedFee, invoice.fee)
        assertEquals(expectedTaxAmount, invoice.taxAmount)
        assertEquals(expectedTotalAmount, invoice.totalAmount)
    }
}
