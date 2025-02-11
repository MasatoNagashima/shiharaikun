package com.shiharaikun.usecase.user

import com.shiharaikun.domain.user.User
import com.shiharaikun.domain.user.UserDataSource

class SignInUseCase(
    private val userDataSource: UserDataSource,
) {
    suspend fun execute(
        email: String,
        password: String,
    ): User? = userDataSource.verifyUser(email, password)
}
