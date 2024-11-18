package com.niderkvel.iskraapispring.models

import com.niderkvel.iskraapispring.TABLE_SUBTASK
import jakarta.persistence.*

@Entity(name = TABLE_SUBTASK)
data class Subtask(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = -1,
    @Column(columnDefinition = "TEXT")
    val question: String,
    val type: Byte,
    @OneToMany(cascade = [CascadeType.REMOVE])
    val options: List<Option> = emptyList(),
)
