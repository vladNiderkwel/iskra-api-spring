package com.niderkvel.iskraapispring.models

import com.niderkvel.iskraapispring.TABLE_TASK
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity(name = TABLE_TASK)
data class Task (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = -1,
    val title: String,
    @OneToMany(cascade = [CascadeType.REMOVE])
    var subtasks: List<Subtask>,
    var available: Boolean = true,
    val reward: Int = 0
)