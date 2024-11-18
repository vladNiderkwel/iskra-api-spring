package com.niderkvel.iskraapispring.models

import com.niderkvel.iskraapispring.ROLE_TUTOR
import com.niderkvel.iskraapispring.TABLE_STAFF
import jakarta.persistence.*

@Entity(name = TABLE_STAFF)
data class Staff(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = -1,
    val name: String,
    @Column(unique=true)
    val email: String,
    val password: String,
    val role: Byte = ROLE_TUTOR,
    var isBlocked: Boolean = false,
    var isPasswordChanged: Boolean = false
)