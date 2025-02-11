package com.shiharaikun.usecase.invoice

import com.shiharaikun.domain.invoice.Invoice
import com.shiharaikun.domain.invoice.InvoiceDataSource
import java.math.BigDecimal
import java.time.LocalDate

class CreateInvoiceUseCase(
    private val invoiceDataSource: InvoiceDataSource,
) {
    suspend fun execute(
        userId: String,
        issueDateString: String,
        paymentAmountString: String,
        paymentDueDateString: String,
    ): Boolean {
        val issueDate: LocalDate = LocalDate.parse(issueDateString)
        val paymentAmount: BigDecimal = BigDecimal(paymentAmountString)
        val paymentDueDate: LocalDate = LocalDate.parse(paymentDueDateString)
        val invoice =
            Invoice(
                userId = userId,
                issueDate = issueDate,
                paymentAmount = paymentAmount,
                paymentDueDate = paymentDueDate,
            )
        return invoiceDataSource.insertInvoice(invoice)
    }
}
