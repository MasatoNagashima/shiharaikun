package com.shiharaikun.domain.invoice

import java.time.LocalDate

interface InvoiceDataSource {
    suspend fun insertInvoice(invoice: Invoice): Boolean

    suspend fun getInvoicesByUserId(
        userId: String,
        startDate: LocalDate?,
        endDate: LocalDate?,
    ): List<Invoice>
}
