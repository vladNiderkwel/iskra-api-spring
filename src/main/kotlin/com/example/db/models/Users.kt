package com.example.db.models

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class User(
    val name: String,
    val email: String,
    val password: String,
    val photoUrl: String,
    val role: String
)

object UserTable : IntIdTable("users") {
    val name = varchar(
        name = "name",
        length = 64,
    )
    val email = varchar(
        name = "email",
        length = 64,
    )
    val password = varchar(
        name = "password",
        length = 32
    )
    val photoUrl = varchar(
        name = "photo_url",
        length = 128
    )
    val role = varchar(
        name = "role",
        length = 8
    )
}

class UserService(db: Database) {
    init {
        transaction(db) {
            SchemaUtils.create(UserTable)
        }
    }

    private fun toUser(row: ResultRow): User = User(
        name = row[UserTable.name],
        email = row[UserTable.email],
        password = row[UserTable.password],
        photoUrl = row[UserTable.photoUrl],
        role = row[UserTable.role],
    )

    private suspend fun <T> query(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(user: User): Int = query {
        UserTable.insert {
            it[name] = user.name
            it[email] = user.email
            it[password] = user.password
            it[photoUrl] = user.photoUrl
            it[role] = user.role
        }[UserTable.id].value
    }

    suspend fun all(): List<User> = query {
        UserTable.selectAll().map(::toUser)
    }

    suspend fun find(id: Int): User? = query {
        UserTable
            .select { UserTable.id eq id }
            .map(::toUser)
            .singleOrNull()
    }

    suspend fun delete(id: Int): Boolean = query {
        UserTable.deleteWhere {
            UserTable.id eq id
        } > 0
    }

    suspend fun update(id: Int, user: User): Boolean = query {
        UserTable.update({ UserTable.id eq id }) {
            it[name] = user.name
            it[email] = user.email
            it[password] = user.password
            it[photoUrl] = user.photoUrl
            it[role] = user.role
        } > 0
    }
}