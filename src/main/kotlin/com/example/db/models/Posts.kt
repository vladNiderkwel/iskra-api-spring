package com.example.db.models

import com.example.POST_TABLE
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Post(
    val id: Int = -1,
    val title: String,
    val body: String,
    val author: User
)

class PostEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PostEntity>(PostTable)

    var title by PostTable.title
    var body by PostTable.body
    var author by UserEntity referencedOn PostTable.author

    fun model(): Post = Post(
        id = id.value,
        title = title,
        body = body,
        author = author.model()
    )
}

object PostTable : IntIdTable(POST_TABLE) {
    val title = varchar(
        name = "title",
        length = 255
    )
    val body = text(
        name = "body"
    )
    val author = reference(
        name = "author",
        foreign = UserTable
    )
}