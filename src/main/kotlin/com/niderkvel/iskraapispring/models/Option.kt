package com.niderkvel.iskraapispring.models

import com.niderkvel.iskraapispring.TABLE_OPTION
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = TABLE_OPTION)
data class Option(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = -1,
    val text: String,
    val isAnswer: Boolean = false,
)