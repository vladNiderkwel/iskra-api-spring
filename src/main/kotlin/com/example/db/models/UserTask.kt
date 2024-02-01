package com.example.db.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

data class UserTask(
    val user: User,
    val tasks: List<Task>
)

class UserTaskEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserTaskEntity>(UserTaskTable)

    var user by UserEntity referencedOn UserAchievementTable.user
    var tasks by TaskEntity via TaskTable

    fun toUserTask(): UserTask = UserTask(
        user = user.toUser(),
        tasks = tasks.map { it.toTask() }
    )
}

object UserTaskTable : IntIdTable("USER_TASKS") {
    val user = reference("user", UserTable)
    val tasks = reference("tasks", TaskTable)
}