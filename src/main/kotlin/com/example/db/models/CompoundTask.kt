package com.example.db.models

import com.example.db.TaskTypes
import com.example.db.query
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

@Serializable
data class CompoundTask(
    val id: Int = -1,
    val task: Task,
    val subTasks: List<Task>
)

class CompoundTaskEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CompoundTaskEntity>(CompoundTaskTable)

    var task by TaskEntity referencedOn CompoundTaskTable.task
    var subTasks by TaskEntity via TaskTable

    fun toCompoundTask(): CompoundTask = CompoundTask(
        id = id.value,
        task = task.toTask(),
        subTasks = subTasks.map { it.toTask() }
    )
}

object CompoundTaskTable : IntIdTable("COMPOUND_TASKS") {
    val task = reference("task", TaskTable)
    val subTasks = reference("sub_tasks", TaskTable)
}

class CompoundTaskController {
    suspend fun create(cTask: CompoundTask): Int = query {
        val mainTask = TaskEntity.new {
            name = cTask.task.name
            type = cTask.task.type
            startDate = cTask.task.startDate
            endDate = cTask.task.endDate
        }

        val subtasks = mutableListOf<TaskEntity>()

        for (t in cTask.subTasks) {
            subtasks.add(
                TaskEntity.new {
                    name = t.name
                    type = t.type
                    startDate = t.startDate
                    endDate = t.endDate
                }
            )

            when (t.type) {
                TaskTypes.QUESTION.value ->
                    QuestionTaskEntity.new {
                        TODO()
                    }
            }
        }

        CompoundTaskEntity.new {
            task = mainTask
            subTasks = SizedCollection(subtasks)
        }.id.value
    }

    suspend fun all(): List<CompoundTask> = query {
        CompoundTaskEntity
            .all()
            .map { it.toCompoundTask() }
    }

    suspend fun find(id: Int): CompoundTask? = query {
        CompoundTaskEntity.findById(id)?.toCompoundTask()
    }

    suspend fun find(task: Task): CompoundTask? = query {
        CompoundTaskEntity
            .find {
                TODO()
                //CompoundTaskTable.task eq TaskEntity.findById(task.id)
            }
            .singleOrNull()
            ?.toCompoundTask()
    }

    suspend fun update(id: Int, cTask: CompoundTask) = query {
        val subtasks = mutableListOf<TaskEntity>()

        cTask.subTasks.forEach {
            subtasks.add(TaskEntity.findById(it.id)!!)
        }

        CompoundTaskEntity.findById(id)?.let {
            it.task = TaskEntity.findById(cTask.task.id)!!
            it.subTasks = SizedCollection(subtasks)
        }
    }

    suspend fun delete(id: Int) = query {
        CompoundTaskEntity.findById(id)?.delete()
    }
}