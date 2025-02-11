package com.shiharaikun.domain.user

interface UserDataSource {
    suspend fun insertUser(
        user: User,
        password: String,
    ): Boolean

    suspend fun verifyUser(
        email: String,
        password: String,
    ): User?
}
