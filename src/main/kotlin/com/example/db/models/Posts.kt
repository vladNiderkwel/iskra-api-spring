package com.example.db.models

import com.example.plugins.LocalDateSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime


@Serializable
data class Post(
    val title: String,
    val body: String,
    val photoUrl: String,
    @Serializable(with = LocalDateSerializer::class)
    val publicationDate: LocalDateTime
)

class PostEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PostEntity>(PostTable)
    var title by PostTable.title
    var body by PostTable.body
    var photoUrl by PostTable.photoUrl
    var publicationDate by PostTable.publicationDate

    fun toPost(): Post = Post(
        title = title,
        body = body,
        photoUrl = photoUrl,
        publicationDate = publicationDate,
    )
}

object PostTable : IntIdTable("POSTS") {
    val title = varchar(
        name = "title",
        length = 64,
    )
    val body = text("body")
    val photoUrl = varchar(
        name = "photo_url",
        length = 64,
    )
    val publicationDate = datetime("publication_date")
}