package com.shiharaikun.infra.invoice

import com.shiharaikun.domain.invoice.Invoice
import com.shiharaikun.domain.invoice.InvoiceDataSource
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

object Invoices : Table("invoices") {
    val invoiceId = varchar("invoice_id", 36).uniqueIndex()
    val userId = varchar("user_id", 36)
    val issueDate = date("issue_date")
    val paymentAmount = decimal("payment_amount", 15, 2)
    val feeRate = decimal("fee_rate", 5, 2)
    val taxRate = decimal("tax_rate", 5, 2)
    val paymentDueDate = date("payment_due_date")
    val fee = decimal("fee", 15, 2)
    val taxAmount = decimal("tax_amount", 15, 2)
    val totalAmount = decimal("total_amount", 15, 2)
    val isDeleted = bool("is_deleted").default(false)
    val createdAt =
        timestamp("created_at").defaultExpression(
            org.jetbrains.exposed.sql.javatime
                .CurrentTimestamp(),
        )
    val updatedAt =
        timestamp("updated_at").defaultExpression(
            org.jetbrains.exposed.sql.javatime
                .CurrentTimestamp(),
        )
    val deletedAt = timestamp("deleted_at").nullable()

    override val primaryKey = PrimaryKey(invoiceId)
}

class PostgresInvoiceDataSource : InvoiceDataSource {
    init {
        transaction {
            SchemaUtils.create(Invoices)
        }
    }

    override suspend fun insertInvoice(invoice: Invoice): Boolean =
        transaction {
            Invoices
                .insert {
                    it[invoiceId] = invoice.invoiceId
                    it[userId] = invoice.userId
                    it[issueDate] = invoice.issueDate
                    it[paymentAmount] = invoice.paymentAmount
                    it[feeRate] = invoice.feeRate
                    it[taxRate] = invoice.taxRate
                    it[paymentDueDate] = invoice.paymentDueDate
                    it[fee] = invoice.fee
                    it[taxAmount] = invoice.taxAmount
                    it[totalAmount] = invoice.totalAmount
                    it[isDeleted] = invoice.isDeleted
                    it[createdAt] = invoice.createdAt
                    it[updatedAt] = invoice.updatedAt
                    it[deletedAt] = invoice.deletedAt
                }.insertedCount > 0
        }

    override suspend fun getInvoicesByUserId(
        userId: String,
        startDate: LocalDate?,
        endDate: LocalDate?,
    ): List<Invoice> =
        transaction {
            Invoices
                .select {
                    (Invoices.userId eq userId) and
                    (startDate?.let { Invoices.paymentDueDate.greaterEq(it) } ?: Op.TRUE) and
                    (endDate?.let { Invoices.paymentDueDate.lessEq(it) } ?: Op.TRUE)
                }
                .map { row -> rowToInvoice(row) }
        }
    }

private fun rowToInvoice(row: ResultRow): Invoice =
    Invoice(
        invoiceId = row[Invoices.invoiceId],
        userId = row[Invoices.userId],
        issueDate = row[Invoices.issueDate],
        paymentAmount = row[Invoices.paymentAmount],
        feeRate = row[Invoices.feeRate],
        taxRate = row[Invoices.taxRate],
        paymentDueDate = row[Invoices.paymentDueDate],
        isDeleted = row[Invoices.isDeleted],
        createdAt = row[Invoices.createdAt],
        updatedAt = row[Invoices.updatedAt],
        deletedAt = row[Invoices.deletedAt],
    )
