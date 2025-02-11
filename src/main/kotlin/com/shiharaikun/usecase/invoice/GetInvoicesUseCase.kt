package com.shiharaikun.usecase.invoice

import com.shiharaikun.domain.invoice.Invoice
import com.shiharaikun.domain.invoice.InvoiceDataSource
import java.time.LocalDate

class GetInvoicesUseCase(
    private val invoiceDataSource: InvoiceDataSource,
) {
    suspend fun execute(
        userId: String,
        startDateString: String?,
        endDateString: String?,
    ): List<InvoiceDto> {
        val startDate =
            if (startDateString != null) {
                LocalDate.parse(startDateString)
            } else {
                null
            }

        val endDate =
            if (endDateString != null) {
                LocalDate.parse(endDateString)
            } else {
                null
            }

        val invoices = invoiceDataSource.getInvoicesByUserId(
            userId,
            startDate,
            endDate,
        )
        return invoices.map { invoice ->
            InvoiceDto(
                invoiceId = invoice.invoiceId,
                userId = invoice.userId,
                issueDate = invoice.issueDate.toString(),
                paymentAmount = invoice.paymentAmount.toString(),
                feeRate = invoice.feeRate.toString(),
                taxRate = invoice.taxRate.toString(),
                paymentDueDate = invoice.paymentDueDate.toString()
            )
        }
    }
}
