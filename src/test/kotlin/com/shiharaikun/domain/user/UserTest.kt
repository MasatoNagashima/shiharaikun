package com.shiharaikun.domain.user

import com.shiharaikun.domain.user.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Instant

class UserTest {
    @Test
    fun `should create user with default properties`() {
        val user =
            User(
                name = "John Doe",
                email = "johndoe@example.com",
                companyName = "Company Inc.",
            )

        assertNotNull(user.userId) // userId should be automatically generated
        assertEquals("John Doe", user.name)
        assertEquals("johndoe@example.com", user.email)
        assertEquals("Company Inc.", user.companyName)
        assertFalse(user.isDeleted)
        assertNotNull(user.createdAt) // createdAt should be automatically set to current time
        assertNotNull(user.updatedAt) // updatedAt should be automatically set to current time
        assertNull(user.deletedAt) // deletedAt should be null by default
    }

    @Test
    fun `should create user with deleted status`() {
        val deletedAt = Instant.now()
        val user =
            User(
                name = "Jane Doe",
                email = "janedoe@example.com",
                companyName = "Company Inc.",
                isDeleted = true,
                deletedAt = deletedAt,
            )

        assertNotNull(user.userId)
        assertTrue(user.isDeleted) // isDeleted should be true
        assertEquals(deletedAt, user.deletedAt) // deletedAt should match the provided value
    }

    @Test
    fun `should check user ID is unique`() {
        val user1 =
            User(
                name = "John Doe",
                email = "johndoe@example.com",
                companyName = "Company Inc.",
            )
        val user2 =
            User(
                name = "Jane Doe",
                email = "janedoe@example.com",
                companyName = "Another Company",
            )

        // userId should be unique for each user
        assertNotEquals(user1.userId, user2.userId)
    }

    @Test
    fun `should check updatedAt is different from createdAt`() {
        val user =
            User(
                name = "John Doe",
                email = "johndoe@example.com",
                companyName = "Company Inc.",
            )

        assertNotEquals(user.createdAt, user.updatedAt) // updatedAt should be different from createdAt
    }
}
