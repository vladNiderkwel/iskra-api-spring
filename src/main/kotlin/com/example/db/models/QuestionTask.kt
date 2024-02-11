package com.example.db.models

import com.example.db.query
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

@Serializable
data class QuestionTask(
    val id: Int = -1,
    val task: Task,
    val question: String,
    val answer: String
)

class QuestionTaskEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<QuestionTaskEntity>(QuestionTaskTable)

    var task by TaskEntity referencedOn QuestionTaskTable.task
    var question by QuestionTaskTable.question
    var answer by QuestionTaskTable.answer

    fun toQuestionTask(): QuestionTask = QuestionTask(
        id = id.value,
        task = task.toTask(),
        question = question,
        answer = answer,
    )
}

object QuestionTaskTable : IntIdTable("QUESTION_TASKS") {
    val task = reference("task", TaskTable)
    val question = text("question")
    val answer = text("answer")
}

class QuestionTaskController {
    suspend fun create(qTask: QuestionTask): Int = query {
        QuestionTaskEntity.new {
            task = TaskEntity.new {
                name = qTask.task.name
                type = qTask.task.type
                startDate = qTask.task.startDate
                endDate = qTask.task.endDate
            }
            answer = qTask.answer
            question = qTask.question
        }.id.value
    }

    suspend fun all(): List<QuestionTask> = query {
        QuestionTaskEntity
            .all()
            .map { it.toQuestionTask() }
    }

    suspend fun find(id: Int): QuestionTask? = query {
        QuestionTaskEntity.findById(id)?.toQuestionTask()
    }

    suspend fun update(id: Int, qTask: QuestionTask) = query {
        QuestionTaskEntity.findById(id)?.let {
            it.task = TaskEntity.findById(qTask.task.id)!!
            it.question = qTask.question
            it.answer = qTask.answer
        }
    }

    suspend fun delete(id: Int) = query {
        QuestionTaskEntity.findById(id)?.delete()
    }
}