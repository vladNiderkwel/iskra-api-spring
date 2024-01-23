package com.example.db.models

import com.example.db.models.CompoundTaskEntity.Companion.referrersOn
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class CompoundTask(
    val task: Task,
    val subTasks: List<Task>
)

class CompoundTaskEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CompoundTaskEntity>(CompoundTaskTable)
    var task by TaskEntity referencedOn CompoundTaskTable.task
    var subTasks by TaskEntity via TaskTable

    fun toCompoundTask(): CompoundTask = CompoundTask(
        task = task.toTask(),
        subTasks = subTasks.map { entity ->
            Task(
                name = entity.name,
                type = entity.type,
                startDate = entity.startDate,
                endDate = entity.endDate,
            )
        }
    )
}

object CompoundTaskTable : IntIdTable("COMPOUND_TASKS") {
    val task = reference("task", TaskTable)
    val subTasks = reference("sub_tasks", TaskTable)
}