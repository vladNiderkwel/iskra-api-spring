package com.example.db.models

import com.example.db.query
import com.example.plugins.LocalDateSerializer
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

@Serializable
data class Task(
    val id: Int = -1,
    val name: String,
    val type: Byte,
    @Serializable(with = LocalDateSerializer::class)
    val startDate: LocalDateTime,
    @Serializable(with = LocalDateSerializer::class)
    val endDate: LocalDateTime
)

class TaskEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TaskEntity>(TaskTable)

    var name by TaskTable.name
    var type by TaskTable.type
    var startDate by TaskTable.startDate
    var endDate by TaskTable.endDate

    fun toTask(): Task = Task(
        id = id.value,
        name = name,
        type = type,
        startDate = startDate,
        endDate = endDate
    )
}

object TaskTable : IntIdTable("TASKS") {
    val name = varchar(
        name = "name",
        length = 64,
    )
    val type = byte(
        name = "type",
    )
    val startDate = datetime(
        name = "start_date"
    )
    val endDate = datetime(
        name = "end_date"
    )
}

class TaskController {
    suspend fun create(task: Task): Int = query {
        TaskEntity.new {
            name = task.name
            type = task.type
            startDate = task.startDate
            endDate = task.endDate
        }.id.value
    }

    suspend fun all(): List<Task> = query {
        TaskEntity
            .all()
            .map { it.toTask() }
    }

    suspend fun find(id: Int): Task? = query {
        TaskEntity.findById(id)?.toTask()
    }

    suspend fun update(id: Int, task: Task) = query {
        TaskEntity.findById(id)?.let {
            it.name = task.name
            it.type = task.type
            it.startDate = task.startDate
            it.endDate = task.endDate
        }
    }

    suspend fun delete(id: Int) = query {
        TaskEntity.findById(id)?.delete()
    }
}