package com.example.db.models

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

@Serializable
data class Question(
    val author: User,
    val question: String,
    val answer: String = "",
    val phase: Byte
)

class QuestionEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<QuestionEntity>(QuestionTable)

    var author by UserEntity referencedOn UserAchievementTable.user
    var question by QuestionTable.question
    var answer by QuestionTable.answer
    var phase by QuestionTable.phase

    fun toQuestion(): Question = Question(
        author = author.toUser(),
        question = question,
        answer = answer,
        phase = phase
    )
}

object QuestionTable : IntIdTable("QUESTIONS") {
    val author = reference("author", UserTable)
    val question = text("question")
    val answer = text("answer")
    val phase = byte("phase")
}

class QuestionController {
    private suspend fun <T> query(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(q: Question): Int = query {
        QuestionEntity.new {
            author = UserEntity.findById(q.author.id)!!
            question = q.question
            answer = q.answer
            phase = q.phase
        }.id.value
    }

    suspend fun all(): List<Question> = query {
        QuestionEntity
            .all()
            .map { it.toQuestion() }
    }

    suspend fun find(id: Int): Question? = query {
        QuestionEntity.findById(id)?.toQuestion()
    }

    suspend fun update(id: Int, q: Question) = query {
        QuestionEntity.findById(id)?.let {
            it.author = UserEntity.findById(q.author.id)!!
            it.question = q.question
            it.answer = q.answer
            it.phase = q.phase
        }
    }

    suspend fun delete(id: Int) = query {
        QuestionEntity.findById(id)?.delete()
    }
}