package com.niderkvel.iskraapispring.models

import jakarta.persistence.*

@Entity
data class UserAnswer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = -1,
    @ManyToOne
    val subtask: Subtask,
    @ManyToMany
    val answers: List<Option> = emptyList(),
    val writtenAnswer: String = ""
)