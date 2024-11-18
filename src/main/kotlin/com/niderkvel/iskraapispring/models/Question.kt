package com.niderkvel.iskraapispring.models

import com.niderkvel.iskraapispring.TABLE_QUESTION
import jakarta.persistence.*

@Entity(name = TABLE_QUESTION)
data class Question(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = -1,
    @ManyToOne
    val author: User,
    @Column(columnDefinition = "TEXT")
    val question: String,
    @Column(columnDefinition = "TEXT")
    val answer: String = "",
    val phase: Byte = QuestionPhase.WAITING
)

object QuestionPhase {
    const val WAITING: Byte = 0
    const val ANSWERED: Byte = 1
}