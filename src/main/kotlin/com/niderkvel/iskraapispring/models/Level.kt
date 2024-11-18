package com.niderkvel.iskraapispring.models

import com.niderkvel.iskraapispring.TABLE_LEVEL
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

@Entity(name = TABLE_LEVEL)
data class Level(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = -1,
    @OneToOne
    val user: User,
    var current: Int = 1,
    var expToNext: Int
)