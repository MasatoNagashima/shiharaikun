package com.shiharaikun.domain.user

import java.time.Instant
import java.util.UUID

data class User(
    val userId: String = UUID.randomUUID().toString(),
    val name: String,
    val email: String,
    val companyName: String,
    val isDeleted: Boolean = false,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
    val deletedAt: Instant? = null,
)
