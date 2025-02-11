package com.shiharaikun.infra.user

import com.shiharaikun.domain.user.User
import com.shiharaikun.domain.user.UserDataSource
import com.shiharaikun.usecase.hash.HashingService
import com.shiharaikun.usecase.hash.SaltedHash
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table("users") {
    val userId = varchar("user_id", 36).uniqueIndex()
    val companyName = varchar("company_name", 255)
    val name = varchar("name", 255)
    val email = varchar("email", 255).uniqueIndex()
    val hashedPw = varchar("hashedPw", 255)
    val salt = varchar("salt", 255)
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

    override val primaryKey = PrimaryKey(userId)
}

class PostgresUserDataSource(
    private val hashingService: HashingService,
) : UserDataSource {
    init {
        transaction {
            SchemaUtils.create(Users)
        }
    }

    override suspend fun insertUser(
        user: User,
        password: String,
    ): Boolean =
        transaction {
            // パスワードのハッシュ化・ソルト化
            val saltedHash = hashingService.generateSaltedHash(password)
            Users
                .insert {
                    it[userId] = user.userId
                    it[name] = user.name
                    it[email] = user.email
                    it[companyName] = user.companyName
                    it[hashedPw] = saltedHash.hash
                    it[salt] = saltedHash.salt
                    it[isDeleted] = user.isDeleted
                    it[createdAt] = user.createdAt
                    it[updatedAt] = user.updatedAt
                }.insertedCount > 0
        }

    override suspend fun verifyUser(
        email: String,
        password: String,
    ): User? =
        transaction {
            val userRow =
                Users
                    .select { Users.email eq email and (Users.isDeleted eq false) }
                    .singleOrNull()
            if (userRow == null) {
                // 何かしらのロギング
                return@transaction null
            }

            val isValidPassword =
                hashingService.verify(
                    value = password,
                    saltedHash =
                        SaltedHash(
                            hash = userRow[Users.hashedPw],
                            salt = userRow[Users.salt],
                        ),
                )

            if (isValidPassword == false) {
                // 何かしらのロギング
                return@transaction null
            }

            return@transaction rowToUser(userRow)
        }
}

private fun rowToUser(row: ResultRow): User =
    User(
        userId = row[Users.userId],
        companyName = row[Users.companyName],
        name = row[Users.name],
        email = row[Users.email],
        isDeleted = row[Users.isDeleted],
        createdAt = row[Users.createdAt],
        updatedAt = row[Users.updatedAt],
        deletedAt = row[Users.deletedAt],
    )
