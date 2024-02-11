package com.example.db.models

import com.example.db.query
import com.example.plugins.LocalDateSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.emptySized
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

@Serializable
data class Event(
    val id: Int,
    val author: User,
    val title: String,
    val description: String,
    val members: List<User>,
    @Serializable(with = LocalDateSerializer::class)
    val startDate: LocalDateTime,
    @Serializable(with = LocalDateSerializer::class)
    val endDate: LocalDateTime
)

class EventEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EventEntity>(EventTable)

    var author by UserEntity referencedOn EventTable.author
    var title by EventTable.title
    var description by EventTable.description
    var members by UserEntity via EventTable
    var startDate by EventTable.startDate
    var endDate by EventTable.endDate

    fun toEvent(): Event = Event(
        id = id.value,
        author = author.toUser(),
        title = title,
        description = description,
        members = members.map { it.toUser() },
        startDate = startDate,
        endDate = endDate
    )
}

object EventTable : IntIdTable("EVENTS") {
    val author = reference("author", UserTable)
    val title = varchar(
        name = "title",
        length = 255,
    )
    val description = text("description")
    val members = reference("members", UserTable)
    val startDate = datetime(
        name = "start_date"
    )
    val endDate = datetime(
        name = "end_date"
    )
}

class EventController {
    suspend fun create(event: Event): Int = query {
        EventEntity.new {
            author = UserEntity.findById(event.author.id)!!
            title = event.title
            description = event.description
            members = emptySized()
            startDate = event.startDate
            endDate = event.endDate
        }.id.value
    }

    suspend fun all(): List<Event> = query {
        EventEntity
            .all()
            .map { it.toEvent() }
    }

    suspend fun find(id: Int): Event? = query {
        EventEntity.findById(id)?.toEvent()
    }

    suspend fun update(id: Int, event: Event) = query {
        val membersList = mutableListOf<UserEntity>()

        event.members.forEach {
            membersList.add(
                UserEntity.findById(it.id)!!
            )
        }

        EventEntity.findById(id)?.let {
            it.author = UserEntity.findById(event.author.id)!!
            it.title = event.title
            it.description = event.description
            it.members = SizedCollection(membersList)
            it.startDate = event.startDate
            it.endDate = event.endDate
        }
    }

    suspend fun delete(id: Int) = query {
        EventEntity.findById(id)?.delete()
    }
}