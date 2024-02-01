package com.example.db.models

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

@Serializable
data class Staff(
    val name: String,
    val email: String,
    val password: String,
    val role: Byte
)

class StaffEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<StaffEntity>(StaffTable)

    var name by StaffTable.name
    var email by StaffTable.email
    var password by StaffTable.password
    var role by StaffTable.role

    fun toStaff(): Staff = Staff(
        name = name,
        email = email,
        password = password,
        role = role,
    )
}

object StaffTable : IntIdTable("STAFF") {
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
    val role = byte(
        name = "role",
    )
}

class StaffController {
    private suspend fun <T> query(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(staff: Staff): Int = query {
        StaffEntity.new {
            name = staff.name
            email = staff.email
            password = staff.password
            role = staff.role
        }.id.value
    }

    suspend fun all(): List<Staff> = query {
        StaffEntity
            .all()
            .map { it.toStaff() }
    }

    suspend fun find(id: Int): Staff? = query {
        StaffEntity.findById(id)?.toStaff()
    }

    suspend fun find(email: String): Staff? = query {
        StaffEntity
            .find { StaffTable.email eq email }
            .singleOrNull()
            ?.toStaff()
    }

    suspend fun find(role: Byte): List<Staff> = query {
        StaffEntity
            .find { StaffTable.role eq role }
            .map { it.toStaff() }
    }

    suspend fun update(id: Int, staff: Staff) = query {
        StaffEntity.findById(id)?.let {
            it.name = staff.name
            it.email = staff.email
            it.password = staff.password
            it.role = staff.role
        }
    }

    suspend fun delete(id: Int) = query {
        StaffEntity.findById(id)?.delete()
    }

    suspend fun delete(email: String) = query {
        StaffEntity
            .find { StaffTable.email eq email }
            .singleOrNull()
            ?.delete()
    }
}