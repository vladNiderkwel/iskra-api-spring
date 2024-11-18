package com.niderkvel.iskraapispring.models

import com.niderkvel.iskraapispring.TABLE_USER_TASK
import jakarta.persistence.*

@Entity(name = TABLE_USER_TASK)
data class UserTask(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = -1,
    @ManyToOne
    val user: User,
    @ManyToOne
    val task: Task,
    @OneToMany
    var answers: List<UserAnswer> = emptyList(),
    var result: Float = 0f,
    var status: Byte = UserTaskStatus.WAITING_TO_CHECK
)

object UserTaskStatus {
    const val WAITING_TO_CHECK: Byte = 0
    const val CHECKED: Byte = 1
}