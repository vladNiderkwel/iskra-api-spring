package com.example.db.models

import com.example.USER_TABLE
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class User(
    val id: Int = -1,
    val name: String,
    val email: String,
    val password: String,
    val photoUrl: String
)

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UserTable)

    var name by UserTable.name
    var email by UserTable.email
    var password by UserTable.password
    var photoUrl by UserTable.photoUrl

    fun model(): User =
        User(
            id = id.value,
            name = name,
            email = email,
            password = password,
            photoUrl = photoUrl,
        )
}

object UserTable : IntIdTable(USER_TABLE) {
    val name = varchar(
        name = "name",
        length = 64,
    )
    val email = varchar(
        name = "email",
        length = 64,
    ).uniqueIndex()
    val password = varchar(
        name = "password",
        length = 32
    )
    val photoUrl = varchar(
        name = "photo_url",
        length = 128
    )
}

/*
class UserService(db: Database) {
    init {
        transaction(db) {
            SchemaUtils.create(UserTable)
        }
    }

    private fun toUser(row: ResultRow): User = User(
        id = row[UserTable.id].value,
        name = row[UserTable.name],
        email = row[UserTable.email],
        password = row[UserTable.password],
        photoUrl = row[UserTable.photoUrl],
    )

    private suspend fun <T> query(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(user: User): Int = query {
        UserTable.insert {
            it[name] = user.name
            it[email] = user.email
            it[password] = user.password
            it[photoUrl] = user.photoUrl
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
        } > 0
    }
}
*/