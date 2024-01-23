package com.example.db.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

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
    val photoUrl = varchar(
        name = "photo_url",
        length = 128
    )
    val role = byte(
        name = "role",
    )
}