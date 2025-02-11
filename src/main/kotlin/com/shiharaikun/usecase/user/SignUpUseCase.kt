package com.shiharaikun.usecase.user

import com.shiharaikun.domain.user.User
import com.shiharaikun.domain.user.UserDataSource

class SignUpUseCase(
    private val userDataSource: UserDataSource,
) {
    suspend fun execute(
        name: String,
        email: String,
        password: String,
        companyName: String,
    ): Boolean {
        // ユーザーエンティティを生成
        val user =
            User(
                name = name,
                email = email,
                companyName = companyName,
            )

        // ユーザーをデータベースに挿入
        return userDataSource.insertUser(user, password)
    }
}
