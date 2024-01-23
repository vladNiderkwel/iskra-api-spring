package com.example.db.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class QuestionTask(
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