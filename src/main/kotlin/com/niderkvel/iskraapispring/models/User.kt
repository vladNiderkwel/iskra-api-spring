package com.niderkvel.iskraapispring.models

import com.niderkvel.iskraapispring.TABLE_USER
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = TABLE_USER)
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = -1,
    val name: String,
    val email: String,
    val password: String,
    val photoUrl: String = "photo_placeholder",
    var isBlocked: Boolean = false,
    val isDeleted: Boolean = false,
)
